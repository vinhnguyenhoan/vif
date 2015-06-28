package vn.vif.controller;

import java.util.List;

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

	@RequestMapping(value = "/orderItem/detail/{id}")
	public String orderItemDetail(@PathVariable("id") Long orderItemId,
			HttpServletRequest request, Model uiModel) {
		try {
			OrderItem orderItem = orderItemService.find(orderItemId);
			// TODO check orderItem null
			uiModel.addAttribute("orderItem", orderItem);	
			return "orderItemDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return "orderItemError";
		}
	}
	
	@RequestMapping(value = "/orderItem/detail")
	public String updateOrderItem(@ModelAttribute("orderItem") @Valid OrderItem orderItem,
			HttpServletRequest request, Model uiModel,
			BindingResult bindingResult) {
		validateInputData(orderItem, bindingResult);
		if (!bindingResult.hasErrors()) {
			try {
				if (orderItem.getId() > 0) {
					OrderItem oI = orderItemService.find(orderItem.getId());
					oI.setDesc(orderItem.getDesc());
					oI.setName(orderItem.getName());
					oI.setPrice(orderItem.getPrice());
					oI.setMiniPrice(orderItem.getMiniPrice());
					
					orderItemService.update(oI);
				} else {
					orderItemService.add(orderItem);
				}
				uiModel.addAttribute("success", true);
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
		uiModel.addAttribute("orderItem", orderItem);	
		return "orderItemDetail";
	}

	/*@RequestMapping(value = "/user/add")
	public String addUser(@RequestParam(value="registerId",required=false) Long registerId,
			HttpServletRequest request, Model uiModel) {
		VIFUser me = userService.getLogin();
		if (me == null)
			return "login";
		VIFUser user = new VIFUser();

		uiModel.addAttribute("user", user);
		uiModel.addAttribute("roleList", UserRole.getListUserRole(me));
		
		return "userAdd";
	}*/
	
	/*@RequestMapping(value = "/user/add", params = { "new" })
	public String addNewUser(@ModelAttribute("user") @Valid VIFUser user,
			BindingResult bindingResult, HttpServletRequest request,
			Model uiModel) {
		VIFUser me = userService.getLogin();
		if (me == null)
			return "login";
		
		validateInputData(user, bindingResult, request,"new");

		String nativePass = user.getPassword();
		if (!bindingResult.hasErrors()) {
			// before saving to DB, encrypt the password
			try {
				user.setPassword(encryptPassword(user.getPassword()));
			} catch (NoSuchAlgorithmException nsae) {
				logger.error(nsae.getMessage(), nsae);
				nsae.printStackTrace();
			}
			if (null != user.getBirthdayString() && !user.getBirthdayString().isEmpty()) {
				user.setBirthday(Utility.parseLocalDate(user.getBirthdayString()));
			}
//				if(user.getSendEmail()){
//					user.setActiveCode(GPSUtils.generalCode(user.getId()));
//					user.setIsActive(false);
//				} else {
//					user.setActiveCode("");
//					user.setIsActive(true);
//				}
			user.setCreatedBy(me.getId());
			user.setCreatedDate(new Date());
			try {
				
				userService.add(user);
				
				uiModel.addAttribute("success", true);
				
				return "redirect:/admin/user/detail/" + user.getId();
			} catch (Exception e) {
				e.printStackTrace();
				uiModel.addAttribute("success", false);
				user.setPassword(nativePass);
			}
			
		} else {
			// restore native pass
			user.setPassword(nativePass);
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError fieldError : errors) {
				bindingResult.rejectValue(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			
			
		}
		
		uiModel.addAttribute("user", user);
		uiModel.addAttribute("roleList", UserRole.getListUserRole(me));
			
		return "userAdd";
	}*/

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
}
