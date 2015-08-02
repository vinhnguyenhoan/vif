package vn.vif.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.vif.daos.Filter;
import vn.vif.models.MenuItem;

@Service
public class MenuItemServiceImpl extends GeneralServiceImpl<MenuItem> implements MenuItemService {
	
	@Autowired
	private SettingService settingService;
	
	@Override
	public Class<MenuItem> getEntityClass() {
		return MenuItem.class;
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, List<MenuItem>>[] getMenuItemData() {
		Calendar ca = Calendar.getInstance();
		// get current week
		int currentWeek = ca.get(Calendar.WEEK_OF_YEAR);
		// get start week in setting
		int startWeek = settingService.getSetting().getStartWeek();
		// calculate the week
		final int week = (Math.abs(currentWeek - startWeek) % 4) + 1;
		List<MenuItem> list = list(new Filter() {

			@Override
			public Criteria getCriteria(Session session) {
				Criteria cr = session.createCriteria(getEntityClass());
				cr.add(Restrictions.eq("week", week));
				return cr;
			}
			
		});
		Map<Integer, List<MenuItem>> re = new HashMap<Integer, List<MenuItem>>();
		Map<Integer, List<MenuItem>> reSpec = new HashMap<Integer, List<MenuItem>>();
		for (MenuItem it : list) {
			Integer date = it.getDay();
			List<MenuItem> its;
			if (it.getOrderItem().getSpecItem() != null && it.getOrderItem().getSpecItem()) {
				re = reSpec;
			}
			its = re.get(date);
			if (its == null) {
				its = new ArrayList<MenuItem>();
				re.put(date, its);
			}
			its.add(it);
		}
		return (Map<Integer, List<MenuItem>>[]) new Map[] {re, reSpec};
	}
}
