package vn.vif.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.vif.models.VIFUser;
import vn.vif.services.UserService;

@Controller
public class NavigationController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/layouts/default/navigation")
	public String showNavigation(HttpServletRequest request, Model uiModel) {
		VIFUser user = userService.getLogin();
		if(user != null) {
			uiModel.addAttribute("user", user);
		}
		return "default-navigation";
	}
}
