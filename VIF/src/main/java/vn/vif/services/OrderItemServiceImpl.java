package vn.vif.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.vif.daos.Filter;
import vn.vif.models.OrderItem;
import vn.vif.models.filter.OrderItemListFilter;

@Service
public class OrderItemServiceImpl extends GeneralServiceImpl<OrderItem> implements OrderItemService {
	
	@Autowired
	private SettingService settingService;
	
	@Override
	public Class<OrderItem> getEntityClass() {
		return OrderItem.class;
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, List<OrderItem>>[] getOrderItemData() {
		Calendar ca = Calendar.getInstance();
		// get current week
		int currentWeek = ca.get(Calendar.WEEK_OF_YEAR);
		// get start week in setting
		int startWeek = settingService.getSetting().getStartWeek();
		// calculate the week
		final int week = (Math.abs(currentWeek - startWeek) % 4) + 1;
		List<OrderItem> list = list(new Filter() {

			@Override
			public Criteria getCriteria(Session session) {
				Criteria cr = session.createCriteria(getEntityClass());
				cr.add(Restrictions.eq("week", week));
				return cr;
			}
			
		});
		Map<Integer, List<OrderItem>> re = new HashMap<Integer, List<OrderItem>>();
		Map<Integer, List<OrderItem>> reSpec = new HashMap<Integer, List<OrderItem>>();
		for (OrderItem it : list) {
			putData(re, reSpec, it, it.getMo());
			putData(re, reSpec, it, it.getTu());
			putData(re, reSpec, it, it.getWe());
			putData(re, reSpec, it, it.getTh());
			putData(re, reSpec, it, it.getFr());
			putData(re, reSpec, it, it.getSa());
			putData(re, reSpec, it, it.getSu());
		}
		return (Map<Integer, List<OrderItem>>[]) new Map[] {re, reSpec};
	}

	private boolean putData(Map<Integer, List<OrderItem>> re, Map<Integer, List<OrderItem>> reSpec, OrderItem it, Integer date) {
		if (date != null) {
			List<OrderItem> its;
			if (it.getSpecItem() != null && it.getSpecItem()) {
				re = reSpec;
			}
			its = re.get(date);
			if (its == null) {
				its = new ArrayList<OrderItem>();
				re.put(date, its);
			}
			its.add(it);
			return true;
		}
		return false;
	}

	public List<OrderItem> getOrderItemToday() {
		int dayOfW = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK);
		OrderItemListFilter itemFilter = new OrderItemListFilter();
		itemFilter.setMoveToDate(Arrays.asList(dayOfW));
		itemFilter.setSpecItem(false);
		return list(itemFilter);
	}

	public List<OrderItem> getOrderItemAllDay() {
		OrderItemListFilter itemFilter = new OrderItemListFilter();
		itemFilter.setSpecItem(true);
		return list(itemFilter);
	}
}
