package vn.vif.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.vif.models.VIFUser;
import vn.vif.services.UserService;

@Controller
public class HeaderController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/layouts/default/header", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String showHeader(HttpServletRequest request, Model uiModel) {
		VIFUser user = userService.getLogin();
		if(user != null) {
			uiModel.addAttribute("user", user);
		}
		return "default-header";
	}
}
