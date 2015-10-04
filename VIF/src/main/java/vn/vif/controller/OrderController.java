package vn.vif.controller;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import vn.vif.models.Customer;
import vn.vif.models.District;
import vn.vif.models.OrderLineDetail;
import vn.vif.models.OrderList;
import vn.vif.models.filter.OrderListFilter;
import vn.vif.services.CustomerService;
import vn.vif.services.MenuItemService;
import vn.vif.services.OrderDetailService;
import vn.vif.services.OrderItemService;
import vn.vif.services.OrderService;
import vn.vif.utils.CannotFindByIdException;
import vn.vif.utils.PaginationInfo;
import vn.vif.utils.PaginationUtil;
import vn.vif.utils.VIFUtils;
import vn.vif.utils.converter.OptionItem;

@Controller
@RequestMapping("/admin")
public class OrderController {
	
	@Autowired
	private CustomerController customerController;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderDetailService orderDetailService;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private MenuItemService menuService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private View jsonView_i;
	
	@Autowired
	private ServletContext context;

	@RequestMapping(value = "/order/list", method = {RequestMethod.GET, RequestMethod.POST })
	public String viewListorders(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@ModelAttribute("frmOrderList") @Valid OrderListFilter orderFilter,
			Model uiModel) {
		long count = orderService.count(orderFilter);
		PaginationInfo paginationInfo = PaginationUtil.calculatePage(count, page, size, uiModel);
		List<OrderList> orderList = orderService.list(orderFilter, 
				paginationInfo.getStart(), paginationInfo.getSize());
		
		uiModel.addAttribute("orderList", orderList);
		//uiModel.addAttribute("districtList", convertDistricyListToOptionItem());

		return "orderList";
		// return "redirect:/admin/orderItem/list";
	}

	@RequestMapping(value = "/order/detail/{id}")
	public String orderDetail(@PathVariable("id") Long orderId, HttpServletRequest request, Model uiModel) {
		try {
			OrderList order = orderService.find(orderId);
			if (order == null) {
				return "notFoundError";
			}
			order.setCustomerEditing(order.getCustomer());
			List<OrderLineDetail>[] itemsToday = orderService.getOrderListToday();
			order.updateDetailsForView(itemsToday, orderItemService);
			
			uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
			uiModel.addAttribute("orderList", order);	
			return "orderDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return "orderError";
		}
	}
	
	@RequestMapping(value = "/order/detail", params = { "update" })
	public String updateOrder(@ModelAttribute("orderList") @Valid OrderList order,
			HttpServletRequest request, Model uiModel,
			BindingResult bindingResult) {
		return addUpdateOrder(order, uiModel, bindingResult);
	}

	@RequestMapping(value = "/order/add", params = { "new" }, method = {RequestMethod.GET, RequestMethod.POST })
	public String addNewOrder(@ModelAttribute("orderList") @Valid OrderList order, Model uiModel, BindingResult bindingResult) {
		return addUpdateOrder(order, uiModel, bindingResult);
	}
	
	@Transactional
	private String addUpdateOrder(OrderList order, Model uiModel, BindingResult bindingResult) {
		validateInputData(order, bindingResult);
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError fieldError : errors) {
				bindingResult.rejectValue(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			return handleError(order, uiModel);
		}
		boolean isUpdate = order.getId() != null;
		try {
			if (isUpdate) {
				if (!order.getActive()) {
					// TODO message can not edit active order
					return handleError(order, uiModel);
				}
				OrderList aN = orderService.find(order.getId());
				// remove old order
				if (aN != null) {
					orderService.delete(aN);
					order.setId(null);
				}
//				aN.setNote(order.getNote());
//				aN.setActive(order.getActive());
//				orderService.update(order);
			}
			try {
				order.updateDetailsFromUIModel(orderItemService);
			} catch (CannotFindByIdException cE) {
				uiModel.addAttribute("success", false);
				// TODO throw error
				uiModel.addAttribute("orderList", this);
				return "orderDetail";
			}
			
			order.setCustomer(order.getCustomerEditing());
			if (!isUpdate && order.getCustomer() != null && !VIFUtils.isValid(order.getCustomer().getId())) {
				order.getCustomer().setOverride(order.getOverride());
				// reuse handle customer with customer controller
				// TODO bindingResult new path
				customerController.addUpdateCustomer(order.getCustomer(), uiModel, bindingResult, "customerEditing.");
				// TODO fix null
				Boolean isSuccess = (Boolean) uiModel.asMap().get("success");
				if (isSuccess == null || !isSuccess) {
					uiModel.addAttribute("orderList", order);	
					return "orderDetail";
				}
				order.setAddress(order.getCustomer().getAddressFull());
			}
			Date currentDate = GregorianCalendar.getInstance().getTime();
			order.setCreatedDate(currentDate);
			order.setOrderedDate(currentDate);

			orderService.add(order);
			uiModel.addAttribute("success", true);
			return "redirect:/admin/order/detail/" + order.getId();
		} catch (Exception e) {
			e.printStackTrace();
			return handleError(order, uiModel);
		}
	}

	private String handleError(OrderList order, Model uiModel) {
		// Handle for exception
		uiModel.addAttribute("success", false);
		List<OrderLineDetail>[] itemsToday = orderService.getOrderListToday();
		if (order.getId() != null) {
			order.updateDetailsForView(itemsToday, orderItemService);
		} else {
			order.setTodayDetailLinesFromEditing(itemsToday[0]);
			order.setAllDayDetailLinesFromEditing(itemsToday[1]);
		}
		uiModel.addAttribute("orderList", order);
		return "orderDetail";
	}
	
	@RequestMapping(value = "/order/detailFromCustomer/{id}")
	public String orderDetailFromCustomer(@PathVariable("id") Long customerId, HttpServletRequest request, Model uiModel) {
		try {
			Customer cus = customerService.find(customerId);
			if (cus == null) {
				return "notFoundError";
			}
			OrderList order = createNewOrder();
			order.setCustomerEditing(cus);
			uiModel.addAttribute("orderList", order);
			uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
			return "orderAdd";
		} catch (Exception e) {
			e.printStackTrace();
			return "orderError";
		}
	}
	
	@RequestMapping(value = "/order/add")
	public String addOrder(HttpServletRequest request, Model uiModel) {
		OrderList order = createNewOrder();
		uiModel.addAttribute("orderList", order);
		uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
		return "orderAdd";
	}

	private OrderList createNewOrder() {
		OrderList order = new OrderList();
		
		// Order from admin is active as default
		order.setActive(true);
		List<OrderLineDetail>[] itemsToday = orderService.getOrderListToday();
		
		List<OrderLineDetail> orderListToday = itemsToday[0];
		order.setTodayDetailLines(orderListToday);
		
		List<OrderLineDetail> orderListAllday = itemsToday[1];
		order.setAllDayDetailLines(orderListAllday);
		
		return order;
	}
	
	private void validateInputData(OrderList order, BindingResult bindingResult) {
//		if (order.getDistrictId() == null || order.getDistrictId() <= 0) {
//			bindingResult.rejectValue("districtId", "app_field_empty", new String[]{"Quận"},
//					"empty_error_code");
//		}
//		if (StringUtils.isEmpty(order.getStreet())) {
//			bindingResult.rejectValue("street", "app_field_empty",
//					new Object[]{"Đường"}, "empty_error_code");
//		}
	}
	
	private static final List<OptionItem> convertDistricyListToOptionItem() {
		List<OptionItem> result = new LinkedList<OptionItem>();
		for (District dis : District.values()) {
			result.add(new OptionItem(dis.id, dis.fullName, dis.name));
		}
		return result;
	}
	
}
