package vn.vif.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import vn.vif.models.Setting;

@Service
public class SettingServiceImpl extends GeneralServiceImpl<Setting> implements SettingService {

	@Override
	public Class<Setting> getEntityClass() {
		return Setting.class;
	}

	@Override
	public Setting getSetting() {
		Setting st = find(1);
		if (st == null) {
			st = new Setting();
			st.setId(1l);
			st.setStartWeek(32);
			st.setCompanyName("Công ty Cổ phần Thực phẩm HƯNG GIA");
			st.setPhoneNumber("(08) 6262 44846/680 5042 - 6680 5043 ");
			st.setAddress("B6 Cư xá Thời Báo Kinh Tế Sài Gòn, Lương Định Của Phường Bình Khánh, Quận 2, TP.HCM");
			st.setEndTime(1030);
			add(st);
		}
		return st;
	}
	
	@Override
	public void add(Setting entity) {
		// do nothing
	}
	
	@Override
	public void add(Session session, Setting entity) throws HibernateException {
		// do nothing
	}
	
	@Override
	public void delete(Setting entity) {
		// do nothing
	}
	
	@Override
	public void delete(Session session, Setting entity) throws HibernateException {
		// do nothing
	}
}