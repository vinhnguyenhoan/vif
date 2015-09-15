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
@SuppressWarnings("unchecked")
public class MenuItemServiceImpl extends GeneralServiceImpl<MenuItem> implements MenuItemService {
	
	@Autowired
	private SettingService settingService;
	
	@Override
	public Class<MenuItem> getEntityClass() {
		return MenuItem.class;
	}

	public Map<Integer, List<MenuItem>>[] getMenuItemData() {
		return getMenuItemData(false);
	}
	
	private Map<Integer, List<MenuItem>>[] getMenuItemData(boolean getToday) {
		Calendar ca = Calendar.getInstance();
		// get current week
		int currentWeek = ca.get(Calendar.WEEK_OF_YEAR);
		final int currentDay = ca.get(Calendar.DAY_OF_WEEK);
		// get start week in setting
		int startWeek = settingService.getSetting().getStartWeek();
		// calculate the week
		final int week = (Math.abs(currentWeek - startWeek) % 4) + 1;
		final int nextWeek = (Math.abs(currentWeek + 1 - startWeek) % 4) + 1;
		List<MenuItem> list = list(new Filter() {

			@Override
			public Criteria getCriteria(Session session) {
				Criteria cr = session.createCriteria(getEntityClass());
				cr.add(Restrictions.disjunction()
						.add(
								Restrictions.conjunction()
								.add(Restrictions.eq("week", week))
								.add(Restrictions.ge("day", currentDay))
						).add(
								Restrictions.conjunction()
								.add(Restrictions.eq("week", nextWeek))
								.add(Restrictions.lt("day", currentDay))
						)
						);
				return cr;
			}
			
		});
		Map<Integer, List<MenuItem>> re = new HashMap<Integer, List<MenuItem>>();
		Map<Integer, List<MenuItem>> reSpec = new HashMap<Integer, List<MenuItem>>();
		Map<Integer, List<MenuItem>> map;
		List<MenuItem> its;
		for (MenuItem it : list) {
			Integer date = it.getDay();
			if (Boolean.TRUE.equals(it.getOrderItem().getSpecItem())) {
				map = reSpec;
			} else {
				map = re;
			}
			its = map.get(date);
			if (its == null) {
				its = new ArrayList<MenuItem>();
				map.put(date, its);
			}
			its.add(it);
		}
		return (Map<Integer, List<MenuItem>>[]) new Map[] {re, reSpec};
	}

	public List<MenuItem>[] getOrderListToday() {
		Map<Integer, List<MenuItem>>[] allMenuItems = getMenuItemData(true);
		final int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		return (List<MenuItem>[]) new List[] {allMenuItems[0].get(currentDay), allMenuItems[1].get(currentDay)};
		
//		Calendar ca = Calendar.getInstance();
//		// get current week
//		int currentWeek = ca.get(Calendar.WEEK_OF_YEAR);
//		final int currentDay = ca.get(Calendar.DAY_OF_WEEK);
//		// get start week in setting
//		int startWeek = settingService.getSetting().getStartWeek();
//		// calculate the week
//		final int week = (Math.abs(currentWeek - startWeek) % 4) + 1;
//		List<MenuItem> list = list(new Filter() {
//
//			@Override
//			public Criteria getCriteria(Session session) {
//				Criteria cr = session.createCriteria(getEntityClass());
//				cr.add(Restrictions.eq("week", week))
//				.add(Restrictions.ge("day", currentDay));
//				return cr;
//			}
//			
//		});
//		return list;
	}
}
