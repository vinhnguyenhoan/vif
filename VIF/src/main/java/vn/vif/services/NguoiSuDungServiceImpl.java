package vn.vif.services;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.vif.models.NguoiSuDung;

@Service
public class NguoiSuDungServiceImpl extends GeneralServiceImpl<NguoiSuDung>
		implements NguoiSuDungService {
	@Autowired
	protected SessionFactory sessionFactory;

	public Class<NguoiSuDung> getEntityClass() {
		return NguoiSuDung.class;
	}
	
	public NguoiSuDung findByUsername(String username) {

		String[] fieldNames = new String[1];
		fieldNames[0] = "maDangNhap";
		String[] fieldValues = new String[1];
		fieldValues[0] = username;

		List<NguoiSuDung> userList = list(fieldNames, fieldValues, false, null,
				0, 1);
		if (null != userList && !userList.isEmpty()) {
			return userList.get(0);
		}
		return null;
	}
}
