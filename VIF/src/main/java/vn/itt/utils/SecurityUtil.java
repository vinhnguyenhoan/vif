package vn.itt.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import vn.itt.models.NguoiSuDung;
import vn.itt.services.NguoiSuDungService;

@Repository
public class SecurityUtil {

	@Autowired
	private NguoiSuDungService nguoiSuDungService;

	public NguoiSuDung getCurrentUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<NguoiSuDung> dsNguoiSuDungs = nguoiSuDungService.list(new String[] { "maDangNhap" },
				new Object[] { user.getUsername() }, true, null, 0, 1);
		if (dsNguoiSuDungs.size() > 0) {
			return dsNguoiSuDungs.get(0);
		}
		return null;
	}
}
