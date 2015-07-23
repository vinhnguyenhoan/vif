package vn.vif.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import vn.vif.models.Customer;
import vn.vif.models.filter.CustomerListFilter;
import vn.vif.services.CustomerService;
import vn.vif.utils.PaginationInfo;
import vn.vif.utils.PaginationUtil;

@Controller
@RequestMapping("/admin")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private View jsonView_i;
	
	@Autowired
	private ServletContext context;

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
		//uiModel.addAttribute("districtList", convertDistricyListToOptionItem());

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
//			uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
			uiModel.addAttribute("customer", customer);	
			return "customerDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return "orderItemError";
		}
	}
	
//	@RequestMapping(value = "/customer/detail", params = { "update" })
//	public String updateCustomer(@ModelAttribute("customer") @Valid Customer customer,
//			HttpServletRequest request, Model uiModel,
//			BindingResult bindingResult) {
//		return addUpdateCustomer(customer, uiModel, bindingResult);
//	}
//
//	@RequestMapping(value = "/customer/add", params = { "new" })
//	public String addNewCustomer(@ModelAttribute("customer") @Valid Customer customer,
//			HttpServletRequest request, Model uiModel,
//			BindingResult bindingResult) {
//		return addUpdateCustomer(customer, uiModel, bindingResult);
//	}
//	
//	private String addUpdateCustomer(Customer customer, Model uiModel, BindingResult bindingResult) {
//		validateInputData(customer, bindingResult);
//		if (!bindingResult.hasErrors()) {
//			try {
//				if (customer.getId() != null) {
//					Customer aN = customerService.find(customer.getId());
//					aN.setAddress(customer.getAddress());
//					aN.setDistrictId(customer.getDistrictId());
//					aN.setLevel(customer.getLevel());
//					aN.setOfficeName(customer.getOfficeName());
//					aN.setStreet(customer.getStreet());
//					addressNoteService.update(aN);
//				} else {
//					addressNoteService.add(customer);
//				}
//				uiModel.addAttribute("success", true);
//				uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
//				return "redirect:/admin/addressNote/detail/" + customer.getId();
//			} catch (Exception e) {
//				e.printStackTrace();
//				uiModel.addAttribute("success", false);
//			}
//		} else {
//			List<FieldError> errors = bindingResult.getFieldErrors();
//			for (FieldError fieldError : errors) {
//				bindingResult.rejectValue(fieldError.getField(),
//						fieldError.getDefaultMessage());
//			}
//			
//		}
//		// Handle for exception
//		uiModel.addAttribute("addressNote", customer);	
//		return "addressNoteDetail";
//	}
//	
//	@RequestMapping(value = "/addressNote/add")
//	public String addAddressNote(HttpServletRequest request, Model uiModel) {
//		AddressNote addressNote = new AddressNote();
//		uiModel.addAttribute("addressNote", addressNote);
//		uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
//		return "addressNoteAdd";
//	}
//
//	private void validateInputData(AddressNote addressNote, BindingResult bindingResult) {
	// validate format phone, email. Not empty name, phone, address
//		if (addressNote.getDistrictId() == null || addressNote.getDistrictId() <= 0) {
//			bindingResult.rejectValue("districtId", "app_field_empty", new String[]{"Quận"},
//					"empty_error_code");
//		}
//		if (StringUtils.isEmpty(addressNote.getStreet())) {
//			bindingResult.rejectValue("street", "app_field_empty",
//					new Object[]{"Đường"}, "empty_error_code");
//		}
//	}
//	
//	private static final List<OptionItem> convertDistricyListToOptionItem() {
//		List<OptionItem> result = new LinkedList<>();
//		for (District dis : District.values()) {
//			result.add(new OptionItem(dis.id, dis.fullName, dis.name));
//		}
//		return result;
//	}
}
