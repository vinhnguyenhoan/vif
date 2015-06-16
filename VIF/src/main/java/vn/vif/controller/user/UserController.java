package vn.vif.controller.user;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import vn.vif.models.UserRole;
import vn.vif.models.VIFUser;
import vn.vif.models.filter.UserListFilter;
import vn.vif.services.UserService;
import vn.vif.utils.VIFUtils;
import vn.vif.utils.PaginationInfo;
import vn.vif.utils.PaginationUtil;
import vn.vif.utils.Utility;

@Controller
@RequestMapping("/admin")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private View jsonView_i;
	
	protected final Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/user/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String listUsers(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@ModelAttribute("frmUserList") @Valid UserListFilter userListFilter,
			Model uiModel) {
		VIFUser user = userService.getLogin();
		if (user == null)
			return "login";
		
		
		long count = userService.count(userListFilter);
		PaginationInfo paginationInfo = PaginationUtil.calculatePage(count,
				page, size, uiModel);
		List<VIFUser> userList = userService.list(userListFilter, paginationInfo.getStart(), paginationInfo.getSize());
		
		uiModel.addAttribute("userList", userList);

		return "userList";
	}

	@RequestMapping(value = "/user/detail/{id}")
	public String userDetail(@PathVariable("id") Long userId,
			HttpServletRequest request, Model uiModel) {
		VIFUser me = userService.getLogin();
		if (me == null)
			return "login";
		
		// Load user
		try {
			VIFUser user = userService.find(userId);
			
			if (user == null) {
				return "userError";
			}
			user.setRetypePassword(user.getPassword());
			
			uiModel.addAttribute("user", user);	
			uiModel.addAttribute("roleList", UserRole.getListUserRole(me));
			
			return "userDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return "userError";
		}
	}

	@RequestMapping(value = "/user/detail", params = { "update" })
	public String updateUser(@ModelAttribute("user") @Valid VIFUser user,
			HttpServletRequest request, Model uiModel,
			BindingResult bindingResult) {
		VIFUser me = userService.getLogin();
		VIFUser curUser = userService.find(user.getId());
		
		validateInputData(user, bindingResult, request,"update");
		if (!bindingResult.hasErrors()) {
			if (null != user.getBirthdayString() && !user.getBirthdayString().isEmpty()) {
				curUser.setBirthday(Utility.parseLocalDate(user.getBirthdayString()));
			}
			curUser.setUpdatedBy(me.getId());
			curUser.setUpdatedDate(new Date());
			
			try {
				String newPass = user.getPassword();
				String oldPass = curUser.getPassword();
				if (!newPass.equals(oldPass)) {
					newPass = encryptPassword(newPass);
					user.setPassword(newPass);
					user.setRetypePassword(newPass);
					curUser.setPassword(newPass);
					curUser.setRetypePassword(newPass);
				}
			} catch (NoSuchAlgorithmException nsae) {
				logger.error(nsae.getMessage(), nsae);
				nsae.printStackTrace();
			}
			curUser.setFullName(user.getFullName());
			curUser.setEmail(user.getEmail());
			curUser.setLocked(user.getLocked());
			try {
				
				userService.update(curUser);
				uiModel.addAttribute("success", true);
				
				return "redirect:/admin/user/detail/" + curUser.getId();
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
		
		user = curUser;
		
		user.setRetypePassword(user.getPassword());
		
		uiModel.addAttribute("user", user);	
		uiModel.addAttribute("roleList", UserRole.getListUserRole(me));
		
		return "userDetail";
	}

	@RequestMapping(value = "/user/add")
	public String addUser(@RequestParam(value="registerId",required=false) Long registerId,
			HttpServletRequest request, Model uiModel) {
		VIFUser me = userService.getLogin();
		if (me == null)
			return "login";
		VIFUser user = new VIFUser();

		uiModel.addAttribute("user", user);
		uiModel.addAttribute("roleList", UserRole.getListUserRole(me));
		
		return "userAdd";
	}
	
	
	

	@RequestMapping(value = "/user/add", params = { "new" })
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

	}

	private void validateInputData(VIFUser user,
			BindingResult bindingResult, HttpServletRequest request,String update) {
		
		// Fullname
		String name = user.getFullName();
		if (!VIFUtils.isValid(name)) {
			bindingResult.rejectValue("fullName", "invalid_user_fullname",
					"empty_error_code");
		}
		
		// user name
		String uname = user.getFullName();
		if (!VIFUtils.isValid(uname)) {
			bindingResult.rejectValue("userName", "app_field_empty",
					new Object[]{"Username"}, "empty_error_code");
		}
		
		if (!VIFUtils.isValid(user.getRole())) {
			bindingResult.rejectValue("role", "app_field_empty", new Object[]{"Role"},
					"empty_error_code");
		}
		
		// Password
		String password = user.getPassword();
		if (password == null || password.trim().isEmpty()) {
			bindingResult.rejectValue("password", "invalid_user_pass",
					"empty_error_code");
		}

		// Retype Password
		String retypePassword = user.getRetypePassword();
		if (retypePassword == null || retypePassword.trim().isEmpty()) {
			bindingResult.rejectValue("retypePassword", "invalid_user_retype",
					"empty_error_code");
		} else {
			if (!retypePassword.equals(password))
				bindingResult.rejectValue("retypePassword",
						"user_retype_not_equal_pass", "empty_error_code");
		}

		// Email
		String email = user.getEmail();
		if (email == null || email.trim().isEmpty()) {
			bindingResult.rejectValue("email", "invalid_user_email",
					"empty_error_code");
		}

		// Birthday
		String birthday = user.getBirthdayString();
		if (null != birthday && !birthday.trim().isEmpty()) {
			if (!Utility.isValidDate(birthday))
				bindingResult.rejectValue("birthday", "user_birthday",
						"empty_error_code");
		}
		
	}

	private String encryptPassword(String plainPassword)
			throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(plainPassword.getBytes(), 0,
				plainPassword.length());
		String hashedPass = new BigInteger(1, messageDigest.digest())
				.toString(16);
		if (hashedPass.length() < 32) {
			hashedPass = "0" + hashedPass;
		}

		return hashedPass;
	}

	@RequestMapping(value = "/user/lock", method = RequestMethod.POST)
	public ModelAndView lockUser(@RequestParam("userId") Long userId) {
		String status = "OK";
		try {
			VIFUser user = userService.find(userId);
			if (user != null) {
				user.setLocked(!user.getLocked());
				userService.update(user);
			} else {
				status = "Not OK";
			}
			
		} catch (Exception e) {
			status = "Not OK";
		}
		return new ModelAndView(jsonView_i, "status", status);
	}

	@RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
	public ModelAndView resetPassword(@RequestParam("userId") Long userId) {
		String status = "OK";
		try {
			VIFUser user = userService.find(userId);
			if (user != null) {
				user.setPassword(encryptPassword("123456"));
				userService.update(user);
			}
		} catch (Exception e) {
			status = "Not OK";
		}
		return new ModelAndView(jsonView_i, "status", status);
	}

}