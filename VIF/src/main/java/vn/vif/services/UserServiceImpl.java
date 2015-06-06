package vn.vif.services;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import vn.vif.daos.SecurityUser;
import vn.vif.models.VIFUser;

@Service
public class UserServiceImpl extends GeneralServiceImpl<VIFUser>
		implements UserService {
	@Autowired
	protected SessionFactory sessionFactory;

	public Class<VIFUser> getEntityClass() {
		return VIFUser.class;
	}
	
	public VIFUser getLogin() {
		VIFUser loggedInUser = null;
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		if (principal instanceof SecurityUser){
			SecurityUser userObj=(SecurityUser) principal;
			loggedInUser = userObj.getUser();
		} else {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			loggedInUser = findByUsername(((User) auth.getPrincipal())
					.getUsername());
		}
		return loggedInUser;
	}

	
	public VIFUser findByUsername(String username) {

		String[] fieldNames = new String[1];
		fieldNames[0] = "userName";
		String[] fieldValues = new String[1];
		fieldValues[0] = username;

		List<VIFUser> userList = list(fieldNames, fieldValues, false, null,
				0, 1);
		if (null != userList && !userList.isEmpty()) {
			return userList.get(0);
		}
		return null;
	}
	
}
