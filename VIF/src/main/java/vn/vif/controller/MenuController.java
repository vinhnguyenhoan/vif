package vn.vif.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import vn.vif.models.MenuItem;
import vn.vif.models.OrderItem;
import vn.vif.models.WeekOfMonth;
import vn.vif.models.filter.MenuListFilter;
import vn.vif.services.MenuItemService;
import vn.vif.services.OrderItemService;
import vn.vif.utils.PaginationInfo;
import vn.vif.utils.PaginationUtil;
import vn.vif.utils.VIFUtils;

@Controller
@RequestMapping("/admin")
public class MenuController {
	@Autowired
	private MenuItemService menuItemSevice;
	@Autowired
	private OrderItemService orderItemService;
	
	@RequestMapping(value = "/menu/list", method = { RequestMethod.GET })
	public String viewListOrderItems(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "isAdd", required = false) boolean isAdd,
			@ModelAttribute("menuFilter") @Valid MenuListFilter menuListFilter, Model uiModel) {
		return orderItemsList(page, size, isAdd, menuListFilter, uiModel, false);
	}

	@RequestMapping(value = "/menu/list", method = { RequestMethod.POST })
	public String moveOrderItemsList(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "isAdd", required = false) boolean isAdd,
			@ModelAttribute("menuFilter") @Valid MenuListFilter menuListFilter, Model uiModel) {
		return orderItemsList(page, size, isAdd, menuListFilter, uiModel, true);
	}

	private String orderItemsList(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "isAdd", required = false) boolean isAdd,
			@ModelAttribute("menuFilter") @Valid MenuListFilter menuListFilter, Model uiModel, boolean isSaveDate) {
		// Save before view
		if (isAdd && VIFUtils.isValid(menuListFilter.getAddItem()) 
				&& VIFUtils.isValid(menuListFilter.getDay())
				&& VIFUtils.isValid(menuListFilter.getWeek())) {
			MenuItem menu = new MenuItem();
			menu.setCreatedDate(new Date());
			menu.setOrderItem(new OrderItem(menuListFilter.getAddItem()));
			menu.setDay(menuListFilter.getDay());
			menu.setWeek(menuListFilter.getWeek());
			menuItemSevice.add(menu);
			menuListFilter.setAddItem(null);
		}
		if (VIFUtils.isValid(menuListFilter.getDeleteItem())) {
			menuItemSevice.delete(new MenuItem(menuListFilter.getDeleteItem()));
			menuListFilter.setDeleteItem(null);
		}

		long count = menuItemSevice.count(menuListFilter);
		PaginationInfo paginationInfo = PaginationUtil.calculatePage(count, page, size, uiModel);
		List<MenuItem> orderItemList = menuItemSevice.list(menuListFilter, paginationInfo.getStart(),
				paginationInfo.getSize());

		uiModel.addAttribute("menuList", orderItemList);
		uiModel.addAttribute("dateList", OrderItem.getDataList());
		uiModel.addAttribute("weekList", WeekOfMonth.getList());
		uiModel.addAttribute("orderItemList", orderItemService.list());
//		uiModel.addAttribute("menuFilter", menuListFilter);

		return "menuList";
	}
}
