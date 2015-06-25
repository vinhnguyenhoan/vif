package vn.vif.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import vn.vif.models.OrderItem;
import vn.vif.models.filter.OrderItemListFilter;
import vn.vif.services.OrderItemService;
import vn.vif.utils.PaginationInfo;
import vn.vif.utils.PaginationUtil;

@Controller
@RequestMapping("/admin")
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private View jsonView_i;
	
	@RequestMapping(value = "/orderItem/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String listOrderItems(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@ModelAttribute("frmOrderItemList") @Valid OrderItemListFilter orderItemListFilter,
			Model uiModel) {
		long count = orderItemService.count(orderItemListFilter);
		PaginationInfo paginationInfo = PaginationUtil.calculatePage(count,
				page, size, uiModel);
		List<OrderItem> orderItemList = orderItemService.list(orderItemListFilter, paginationInfo.getStart(), paginationInfo.getSize());
		
		uiModel.addAttribute("orderItemList", orderItemList);
		return "orderItemList";
	}

}
