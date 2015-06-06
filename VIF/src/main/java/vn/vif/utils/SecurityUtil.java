package vn.vif.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import vn.vif.models.VIFUser;
import vn.vif.services.UserService;

@Repository
public class SecurityUtil {

	@Autowired
	private UserService nguoiSuDungService;

	public VIFUser getCurrentUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<VIFUser> dsNguoiSuDungs = nguoiSuDungService.list(new String[] { "userName" },
				new Object[] { user.getUsername() }, true, null, 0, 1);
		if (dsNguoiSuDungs.size() > 0) {
			return dsNguoiSuDungs.get(0);
		}
		return null;
	}
}
