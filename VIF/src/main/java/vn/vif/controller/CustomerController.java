package vn.vif.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.impl.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import vn.vif.models.AddressNote;
import vn.vif.models.Customer;
import vn.vif.models.District;
import vn.vif.models.filter.CustomerListFilter;
import vn.vif.services.AddressNoteService;
import vn.vif.services.CustomerService;
import vn.vif.utils.PaginationInfo;
import vn.vif.utils.PaginationUtil;
import vn.vif.utils.VIFException;
import vn.vif.utils.VIFUtils;
import vn.vif.utils.converter.OptionItem;

@Controller
@RequestMapping("/admin")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AddressNoteService addressNoteService;
	
	@Autowired
	private View jsonView_i;
	
	@Autowired
	private ServletContext context;

	private static final EmailValidator emailValidator = new EmailValidator();

	@RequestMapping(value = "/customer/list", method = {RequestMethod.GET, RequestMethod.POST })
	public String viewListCustomers(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@ModelAttribute("frmCustomerList") @Valid CustomerListFilter customerFilter,
			Model uiModel) {
		long count = customerService.count(customerFilter);
		PaginationInfo paginationInfo = PaginationUtil.calculatePage(count, page, size, uiModel);
		List<Customer> customerList = customerService.list(customerFilter, 
				paginationInfo.getStart(), paginationInfo.getSize());
		
		uiModel.addAttribute("customerList", customerList);
		return "customerList";
		// return "redirect:/admin/orderItem/list";
	}

	@RequestMapping(value = "/customer/detail/{id}")
	public String customerDetail(@PathVariable("id") Long customerId, HttpServletRequest request, Model uiModel) {
		try {
			Customer customer = customerService.find(customerId);
			if (customer == null) {
				return "notFoundError";
			}
			uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
			List<OptionItem> listOI = addressNoteService.listByDistrictIdAsOptionItems(customer.getDistrictId());
			uiModel.addAttribute("addressNoteListSameDistrictId", listOI);
			uiModel.addAttribute("customer", customer);	
			return "customerDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return "orderItemError";
		}
	}
	
	@RequestMapping(value = "/customer/detail", params = { "update" })
	public String updateCustomer(@ModelAttribute("customer") @Valid Customer customer,
			HttpServletRequest request, Model uiModel,
			BindingResult bindingResult) {
		return addUpdateCustomerInternal(customer, uiModel, bindingResult);
	}

	@RequestMapping(value = "/customer/add", params = { "new" })
	public String addNewCustomer(@ModelAttribute("customer") @Valid Customer customer,
			HttpServletRequest request, Model uiModel,
			BindingResult bindingResult) {
		return addUpdateCustomerInternal(customer, uiModel, bindingResult);
	}
	
	private String addUpdateCustomerInternal(Customer customer, Model uiModel, BindingResult bindingResult) {
		return this.addUpdateCustomer(customer, uiModel, bindingResult, null);
	}
	
	public String addUpdateCustomer(Customer customer, Model uiModel, BindingResult bindingResult, String prefixParentFieldName) {
		validateInputData(customer, bindingResult, prefixParentFieldName);
		if (!bindingResult.hasErrors()) {
			try {
				AddressNote aN = null;
				if (customer.getAddressNoteId() != null) {
					aN = addressNoteService.find(customer.getAddressNoteId());
				}
				customer.setAddressNote(aN);
				customer.setPhone(VIFUtils.formatPhoneNumber(customer.getPhone()));
				if (customer.getId() != null || customer.getOverride()) {
					Customer customerFromDB = null;
					if (customer.getOverride()) {
						List<Customer> listCusFromPhone = customerService.list(new String[]{"phone"}, new Object[]{customer.getPhone()}, true, null, -1, -1);
						if (listCusFromPhone != null && listCusFromPhone.size() > 0) {
							customerFromDB = listCusFromPhone.get(0);
							customer.setId(customerFromDB.getId());
						}
					} else {
						customerFromDB = customerService.find(customer.getId());
					}
					customerFromDB.setActive(customer.getActive());
					customerFromDB.setAddress(customer.getAddress());
					customerFromDB.setAddressNote(aN);
					customerFromDB.setAddressNoteId(customer.getAddressNoteId());
					customerFromDB.setDistrictId(customer.getDistrictId());
					customerFromDB.setEmail(customer.getEmail());
					customerFromDB.setName(customer.getName());
					customerFromDB.setNote(customer.getNote());
					customerFromDB.setPhone(customer.getPhone());
					customerService.update(customerFromDB);
				} else {
					customerService.add(customer);
				}
				uiModel.addAttribute("success", true);
				return "redirect:/admin/customer/detail/" + customer.getId();
			} catch (Exception e) {
				e.printStackTrace();
				uiModel.addAttribute("success", false);
				if (e instanceof VIFException) {
					uiModel.addAttribute("error_code", ((VIFException) e).getErrorCode());
				}
			}
		} else {
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError fieldError : errors) {
				bindingResult.rejectValue(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			
		}
		// Handle for exception
		uiModel.addAttribute("customer", customer);	
		return "customerDetail";
	}
	
	@RequestMapping(value = "/customer/add")
	public String addCustomer(HttpServletRequest request, Model uiModel) {
		Customer customer = new Customer();
		uiModel.addAttribute("customer", customer);
		uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
		return "customerAdd";
	}

	private void validateInputData(Customer customer, BindingResult bindingResult, String prefixParentFieldName) {
		if (StringUtils.isBlank(prefixParentFieldName)) {
			prefixParentFieldName = "";
		}
		// TODO check contain customer
		if (StringUtils.isEmpty(customer.getName())) {
			bindingResult.rejectValue(prefixParentFieldName + "name", "app_field_empty", new Object[]{"Tên"}, "empty_error_code");
		}
		if (StringUtils.isEmpty(customer.getPhone())) {
			bindingResult.rejectValue(prefixParentFieldName + "phone", "app_field_empty", new Object[]{"Số điện thoại"}, "empty_error_code");
			// TODO check format phone invalid_phone_number
		} else {
//			String phone = customer.getPhone();
//			phone = phone.trim();
//			if (phone.length() > 15) {
//				bindingResult.rejectValue("phone", "invalid_phone_number", new Object[]{"Số điện thoại"}, "empty_error_code");
//			} else {
//				phone = phone.replace("+84,", "");
//				phone = phone.replace(".", "");
//				phone = phone.replace(",", "");
//			}
			
		}
		if (StringUtils.isEmpty(customer.getEmail())) {
			bindingResult.rejectValue(prefixParentFieldName + "email", "app_field_empty", new Object[]{"Email"}, "empty_error_code");
		} else if (!emailValidator.isValid(customer.getEmail(), null)) {
			bindingResult.rejectValue(prefixParentFieldName + "email", "invalid_user_email", new Object[]{"Email"}, "empty_error_code");
		}
		// TODO check format email
		if (StringUtils.isEmpty(customer.getAddress()) && (customer.getAddressNoteId() == null || customer.getAddressNoteId() <= 0)) {
			bindingResult.rejectValue(prefixParentFieldName + "address", "app_field_empty", new Object[]{"Địa chỉ hoặc Địa chỉ đã lưu"}, "empty_error_code");
			bindingResult.rejectValue(prefixParentFieldName + "addressNoteId", "app_field_empty", new Object[]{"Địa chỉ hoặc Địa chỉ đã lưu"}, "empty_error_code");
		}
		// TODO check unique name ?
	}
	
	private static final List<OptionItem> convertDistricyListToOptionItem() {
		List<OptionItem> result = new LinkedList<OptionItem>();
		for (District dis : District.values()) {
			result.add(new OptionItem(dis.id, dis.fullName, dis.name));
		}
		return result;
	}
}
