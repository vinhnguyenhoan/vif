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
import vn.vif.services.OrderDetailService;
import vn.vif.services.OrderItemService;
import vn.vif.services.OrderService;
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
			List<OrderDetail> details = order.getDetails();
			List<OrderLineDetail> orderListToday = new LinkedList<OrderLineDetail>();
			List<OrderLineDetail> orderListAllday = new LinkedList<OrderLineDetail>();
			
			int index = 0;
			for (OrderDetail detail : details) {
				OrderItem item = null;
				if (detail.getOrderItemId() != null) {
					item = orderItemService.find(detail.getOrderItemId());
				}
				OrderLineDetail lineDetail = new OrderLineDetail(index++, item, detail);
				if (item != null && item.getSpecItem() != null && item.getSpecItem()) {
					orderListAllday.add(lineDetail);
				} else {
					orderListToday.add(lineDetail);
				}
			}
			order.setTodayDetailLines(orderListToday);
			List<Long> listOrderItemId = new ArrayList<Long>(orderListToday.size());
			List<Integer> listOrderItemNumber = new ArrayList<Integer>(orderListToday.size());
			List<Integer> listOrderItemMiniNumber = new ArrayList<Integer>(orderListToday.size());
			List<String> listNote = new ArrayList<String>(orderListToday.size());
			for (OrderLineDetail oL : orderListToday) {
				listOrderItemId.add(oL.getOrderItem().getId());
				listOrderItemNumber.add(oL.getOrderDetail().getNumber());
				listOrderItemMiniNumber.add(oL.getOrderDetail().getMiniNumber());
				listNote.add(oL.getOrderDetail().getNote());
			}
			order.setListOrderItemId(listOrderItemId);
			order.setListNumber(listOrderItemNumber);
			order.setListMiniNumber(listOrderItemMiniNumber);
			order.setListNote(listNote);

			order.setAllDayDetailLines(orderListAllday);
			List<Long> listOrderItemAllDayId = new ArrayList<Long>(orderListAllday.size());
			List<Integer> listOrderItemAllDayNumber = new ArrayList<Integer>(orderListAllday.size());
			List<String> listNoteSpec = new ArrayList<String>(orderListToday.size());
			for (OrderLineDetail oL : orderListAllday) {
				listOrderItemAllDayId.add(oL.getOrderItem().getId());
				listOrderItemAllDayNumber.add(oL.getOrderDetail().getNumber());
				listNoteSpec.add(oL.getOrderDetail().getNote());
			}
			order.setListAllDaytOrderItemId(listOrderItemAllDayId);
			order.setListAllDayNumber(listOrderItemAllDayNumber);
			order.setListNoteSpec(listNoteSpec);
			
			order.setCustomerEditing(order.getCustomer());
			
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
				if (order.getId() != null) {
					OrderList aN = orderService.find(order.getId());
					// TODO handle case update detail ?
					//aN.setDetails(order.getDetails());
					aN.setNote(order.getNote());
					aN.setActive(order.getActive());
					orderService.update(order);
				} else {
					List<OrderDetail> details = new LinkedList<OrderDetail>();
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
						detail.setOrderItemId(itemId);
						detail.setMiniNumber(order.getListMiniNumber().get(index));
						detail.setNumber(order.getListNumber().get(index));
						detail.setMiniPrice(order.getListMiniPrice().get(index));
						detail.setPrice(order.getListPrice().get(index));
						detail.setNote(order.getListNote().get(index));
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
						detail.setOrderItemId(itemId);
						detail.setNumber(order.getListAllDayNumber().get(index));
						detail.setPrice(order.getListAllDayPrice().get(index));
						detail.setNote(order.getListNote().get(index));
						details.add(detail);
						index++;
					}
					order.setDetails(details);
					
					order.setCustomer(order.getCustomerEditing());
					if (order.getCustomer() != null && !VIFUtils.isValid(order.getCustomer().getId())) {
						// reuse handle customer with customer controller
						// TODO bindingResult new path
						customerController.updateCustomer(order.getCustomer(), null, uiModel, bindingResult);
					}
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
		
		// Order from admin is active as default
		order.setActive(true);
		
		List<OrderLineDetail> orderListToday = orderService.getOrderListToday();
		order.setTodayDetailLines(orderListToday);
		List<Long> listOrderItemId = new ArrayList<Long>(orderListToday.size());
		List<Integer> listOrderItemNumber = new ArrayList<Integer>(orderListToday.size());
		List<Integer> listOrderItemPrice = new ArrayList<Integer>(orderListToday.size());
		List<Integer> listOrderItemMiniNumber = new ArrayList<Integer>(orderListToday.size());
		List<Integer> listOrderItemMiniPrice = new ArrayList<Integer>(orderListToday.size());
		List<String> listNote = new ArrayList<String>(orderListToday.size());
		for (OrderLineDetail oL : orderListToday) {
			listOrderItemId.add(oL.getOrderItem().getId());
			listOrderItemNumber.add(oL.getOrderDetail().getNumber());
			listOrderItemPrice.add(oL.getOrderDetail().getPrice());
			listOrderItemMiniNumber.add(oL.getOrderDetail().getMiniNumber());
			listOrderItemMiniPrice.add(oL.getOrderDetail().getMiniPrice());
			listNote.add(oL.getOrderDetail().getNote());
		}
		order.setListOrderItemId(listOrderItemId);
		order.setListNumber(listOrderItemNumber);
		order.setListMiniNumber(listOrderItemMiniNumber);
		order.setListPrice(listOrderItemPrice);
		order.setListMiniPrice(listOrderItemMiniPrice);
		order.setListNote(listNote);
		
		List<OrderLineDetail> orderListAllday = orderService.getOrderListAllDay();
		order.setAllDayDetailLines(orderListAllday);
		
		List<Long> listOrderItemAllDayId = new ArrayList<Long>(orderListAllday.size());
		List<Integer> listOrderItemAllDayNumber = new ArrayList<Integer>(orderListAllday.size());
		List<Integer> listOrderItemAllDayPrice = new ArrayList<Integer>(orderListAllday.size());
		List<String> listNoteSpec = new ArrayList<String>(orderListToday.size());
		for (OrderLineDetail oL : orderListAllday) {
			listOrderItemAllDayId.add(oL.getOrderItem().getId());
			listOrderItemAllDayNumber.add(oL.getOrderDetail().getNumber());
			listOrderItemAllDayPrice.add(oL.getOrderDetail().getPrice());
			listNoteSpec.add(oL.getOrderDetail().getNote());
		}
		order.setListAllDaytOrderItemId(listOrderItemAllDayId);
		order.setListAllDayNumber(listOrderItemAllDayNumber);
		order.setListAllDayPrice(listOrderItemAllDayPrice);
		order.setListNoteSpec(listNoteSpec);
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
