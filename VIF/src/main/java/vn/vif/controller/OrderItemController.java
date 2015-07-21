package vn.vif.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

import vn.vif.models.OrderItem;
import vn.vif.models.filter.OrderItemListFilter;
import vn.vif.services.OrderItemService;
import vn.vif.utils.ImageUtil;
import vn.vif.utils.PaginationInfo;
import vn.vif.utils.PaginationUtil;
import vn.vif.utils.VIFUtils;
import vn.vif.utils.converter.OptionItem;

@Controller
@RequestMapping("/admin")
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private View jsonView_i;
	
	@Autowired
	private ServletContext context;
	
	@RequestMapping(value = "/orderItem/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewListOrderItems(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@ModelAttribute("frmOrderItemList") @Valid OrderItemListFilter orderItemListFilter,
			Model uiModel) {
		return orderItemsList(page, size, orderItemListFilter, uiModel, false);
	}
	
	@RequestMapping(value = "/orderItem/list/saveDate", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String moveOrderItemsList(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@ModelAttribute("frmOrderItemList") @Valid OrderItemListFilter orderItemListFilter,
			Model uiModel) {
		return orderItemsList(page, size, orderItemListFilter, uiModel, true);
	}
	
	private String orderItemsList(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@ModelAttribute("frmOrderItemList") @Valid OrderItemListFilter orderItemListFilter,
			Model uiModel, boolean isSaveDate) {
		// Save before view
		List<Integer> moveToDates = orderItemListFilter.getMoveToDate();
		List<OrderItem> allItem = orderItemListFilter.getItemSelected();
		if (isSaveDate && allItem != null) {
			for (OrderItem orderItem : allItem) {
				if (orderItem.getSelectedToMoveSellDate()) {
					orderItem.setMoveToDate(moveToDates);
					orderItemService.update(orderItem);
				}
			}
		}
		
		long count = orderItemService.count(orderItemListFilter);
		PaginationInfo paginationInfo = PaginationUtil.calculatePage(count,
				page, size, uiModel);
		List<OrderItem> orderItemList = orderItemService.list(orderItemListFilter, paginationInfo.getStart(), paginationInfo.getSize());
		
		orderItemListFilter.setItemSelected(orderItemList);
		uiModel.addAttribute("orderItemList", orderItemList);
		uiModel.addAttribute("dateList", OrderItem.getDataList());

		return "orderItemList";
		// return "redirect:/admin/orderItem/list";
	}

	@RequestMapping(value = "/orderItem/detail/{id}")
	public String orderItemDetail(@PathVariable("id") Long orderItemId,
			HttpServletRequest request, Model uiModel) {
		try {
			OrderItem orderItem = orderItemService.find(orderItemId);
			if (orderItem == null) {
				return "notFoundError";
			}
			uiModel.addAttribute("dateList", OrderItem.getDataList());
			uiModel.addAttribute("orderItem", orderItem);	
			return "orderItemDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return "orderItemError";
		}
	}
	
	@RequestMapping(value = "/orderItem/detail", params = { "update" })
	public String updateOrderItem(@ModelAttribute("orderItem") @Valid OrderItem orderItem,
			HttpServletRequest request, Model uiModel,
			BindingResult bindingResult) {
		return addUpdateOrderItem(orderItem, uiModel, bindingResult);
	}

	@RequestMapping(value = "/orderItem/add", params = { "new" })
	public String addNewOrderItem(@ModelAttribute("orderItem") @Valid OrderItem orderItem,
			HttpServletRequest request, Model uiModel,
			BindingResult bindingResult) {
		return addUpdateOrderItem(orderItem, uiModel, bindingResult);
	}
	
	private String addUpdateOrderItem(OrderItem orderItem, Model uiModel, BindingResult bindingResult) {
		validateInputData(orderItem, bindingResult);
		if (!bindingResult.hasErrors()) {
			try {
				if (orderItem.getId() != null) {
					OrderItem oI = orderItemService.find(orderItem.getId());
					oI.setDesc(orderItem.getDesc());
					oI.setName(orderItem.getName());
					oI.setPrice(orderItem.getPrice());
					oI.setMiniPrice(orderItem.getMiniPrice());
					oI.setMoveToDate(orderItem.getMoveToDate());
					oI.setLogoFile(orderItem.getLogoFile());
					uploadLogo(oI);
					
					orderItemService.update(oI);
				} else {
					orderItemService.add(orderItem);
					if (uploadLogo(orderItem)) {
						orderItemService.update(orderItem);
					}
				}
				uiModel.addAttribute("success", true);
				uiModel.addAttribute("dateList", OrderItem.getDataList());
				return "redirect:/admin/orderItem/detail/" + orderItem.getId();
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
		uiModel.addAttribute("orderItem", orderItem);	
		return "orderItemDetail";
	}
	
	public boolean uploadLogo(OrderItem or) {
		if (or.getLogoFile()!=null && or.getLogoFile().getSize()!=0 && or.getLogoFile().getSize()/1024 < 1000) { // < 500 Kb
			String filename = or.getLogoFile().getOriginalFilename();
			String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length()).toLowerCase();
			if (extension.equals("png") || extension.equals("jpg") || extension.equals("gif")) {
				String logo = "order_item_" + or.getId() + "." + extension;
				String image = "images/pruducts/" + logo;
				logo = File.separator + "images" + File.separator+ "pruducts" + File.separator + logo;
				String path = context.getRealPath("") + logo;
				File folder = new File(path);
				if (!folder.getParentFile().exists()) {
					folder.getParentFile().mkdirs();
				}
				try {
					InputStream is = or.getLogoFile().getInputStream();
					ImageUtil.writeImage(path, is);
					is.close();
					or.setImage(image);
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	@RequestMapping(value = "/orderItem/add")
	public String addOrderItem(HttpServletRequest request, Model uiModel) {
		OrderItem orderItem = new OrderItem();
		uiModel.addAttribute("orderItem", orderItem);
		uiModel.addAttribute("dateList", OrderItem.getDataList());
		return "orderItemAdd";
	}

	private void validateInputData(OrderItem orderItem, BindingResult bindingResult) {
		if (StringUtils.isEmpty(orderItem.getName())) {
			bindingResult.rejectValue("name", "app_field_empty", new String[]{"Tên"},
					"empty_error_code");
		}
		if (orderItem.getPrice() == 0) {
			bindingResult.rejectValue("price", "app_field_empty",
					new Object[]{"Giá"}, "empty_error_code");
		}
	}
	
	@ResponseBody // add to return JSON
	@RequestMapping(value = "/orderItem/getWard")
	public List<OptionItem> getWard(@RequestParam(value = "dist", required = false) Integer dist) {
		if (!VIFUtils.isValid(dist)) {
			return new LinkedList<OptionItem>();
		}
		List<OptionItem> l = OptionItem.NoOptionList(1, "ward 1 of " + dist);
		l.add(new OptionItem(2, "ward 2 of " + dist));
		return l;
	}
}
