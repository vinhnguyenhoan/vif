package vn.vif.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import vn.vif.models.Customer;
import vn.vif.models.District;
import vn.vif.models.OrderDetail;
import vn.vif.models.OrderItem;
import vn.vif.models.OrderLineDetail;
import vn.vif.models.OrderList;
import vn.vif.models.filter.OrderListFilter;
import vn.vif.services.CustomerService;
import vn.vif.services.OrderItemService;
import vn.vif.services.OrderService;
import vn.vif.utils.PaginationInfo;
import vn.vif.utils.PaginationUtil;
import vn.vif.utils.converter.OptionItem;

@Controller
@RequestMapping("/admin")
public class OrderController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderItemService orderItemService;
	
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
	
	private String addUpdateOrder(OrderList order, Model uiModel, BindingResult bindingResult) {
		validateInputData(order, bindingResult);
		if (!bindingResult.hasErrors()) {
			try {
				List<OrderDetail> details = new LinkedList<>();
				List<Long> orderItemIdToday = order.getListOrderItemId();
				int index = 0;
				for (Long itemId : orderItemIdToday) {
					OrderItem item = orderItemService.find(itemId);
					if (item == null) {
						uiModel.addAttribute("success", false);
						// TODO throw error
						uiModel.addAttribute("orderList", order);
						return "orderDetail";
					}
					OrderDetail detail = new OrderDetail();
					detail.setMiniNumber(order.getListMiniNumber().get(index));
					detail.setNumber(order.getListNumber().get(index));
					details.add(detail);
					index++;
				}
				
				List<Long> orderItemIdAllDay = order.getListAllDaytOrderItemId();
				index = 0;
				for (Long itemId : orderItemIdAllDay) {
					OrderItem item = orderItemService.find(itemId);
					if (item == null) {
						uiModel.addAttribute("success", false);
						// TODO throw error
						uiModel.addAttribute("orderList", order);
						return "orderDetail";
					}
					OrderDetail detail = new OrderDetail();
					detail.setNumber(order.getListAllDayNumber().get(index));
					details.add(detail);
					index++;
				}
				order.setDetails(details);
				order.setCustomer(order.getCustomerEditing());
				
				if (order.getId() != null) {
					OrderList aN = orderService.find(order.getId());
					aN.setDetails(order.getDetails());
//					aN.setAddress(order.getAddress());
//					aN.setDistrictId(order.getDistrictId());
//					aN.setLevel(order.getLevel());
//					aN.setOfficeName(order.getOfficeName());
//					aN.setStreet(order.getStreet());
//					orderService.update(aN);
				} else {
					orderService.add(order);
				}
				uiModel.addAttribute("success", true);
//				uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
				return "redirect:/admin/order/detail/" + order.getId();
			} catch (Exception e) {
				e.printStackTrace();
				uiModel.addAttribute("success", false);
			}
		} else {
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError fieldError : errors) {
				bindingResult.rejectValue(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			
		}
		// Handle for exception
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
		List<OrderLineDetail> orderListToday = orderService.getOrderListToday();
		order.setTodayDetailLines(orderListToday);
		List<Long> listOrderItemId = new ArrayList<>(orderListToday.size());
		List<Integer> listOrderItemNumber = new ArrayList<>(orderListToday.size());
		List<Integer> listOrderItemMiniNumber = new ArrayList<>(orderListToday.size());
		for (OrderLineDetail oL : orderListToday) {
			listOrderItemId.add(oL.getOrderItem().getId());
			listOrderItemNumber.add(oL.getOrderDetail().getNumber());
			listOrderItemMiniNumber.add(oL.getOrderDetail().getMiniNumber());
		}
		order.setListOrderItemId(listOrderItemId);
		order.setListNumber(listOrderItemNumber);
		order.setListMiniNumber(listOrderItemMiniNumber);
		
		List<OrderLineDetail> orderListAllday = orderService.getOrderListAllDay();
		order.setAllDayDetailLines(orderListAllday);
		
		List<Long> listOrderItemAllDayId = new ArrayList<>(orderListAllday.size());
		List<Integer> listOrderItemAllDayNumber = new ArrayList<>(orderListAllday.size());
		for (OrderLineDetail oL : orderListAllday) {
			listOrderItemAllDayId.add(oL.getOrderItem().getId());
			listOrderItemAllDayNumber.add(oL.getOrderDetail().getNumber());
		}
		order.setListAllDaytOrderItemId(listOrderItemAllDayId);
		order.setListAllDayNumber(listOrderItemAllDayNumber);
		
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
		List<OptionItem> result = new LinkedList<>();
		for (District dis : District.values()) {
			result.add(new OptionItem(dis.id, dis.fullName, dis.name));
		}
		return result;
	}
	
}
