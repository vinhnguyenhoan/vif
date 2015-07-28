package vn.vif.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import vn.vif.models.OrderItem;
import vn.vif.models.filter.OrderItemListFilter;

@Service
public class OrderItemServiceImpl extends GeneralServiceImpl<OrderItem> implements OrderItemService {

	@Override
	public Class<OrderItem> getEntityClass() {
		return OrderItem.class;
	}

	public Map<Integer, List<OrderItem>> getOrderItemData() {
		List<OrderItem> list = list();
		Map<Integer, List<OrderItem>> re = new HashMap<Integer, List<OrderItem>>();
		for (OrderItem it : list) {
			boolean hasDate = false;
			if (putData(re, it, it.getMo())) {
				hasDate = true;
			}
			if (putData(re, it, it.getTu())) {
				hasDate = true;
			}
			if (putData(re, it, it.getWe())) {
				hasDate = true;
			}
			if (putData(re, it, it.getTh())) {
				hasDate = true;
			}
			if (putData(re, it, it.getFr())) {
				hasDate = true;
			}
			if (putData(re, it, it.getSa())) {
				hasDate = true;
			}
			if (putData(re, it, it.getSu())) {
				hasDate = true;
			}
			if (!hasDate) {
				putData(re, it, -1);
			}
		}
		return re;
	}

	private boolean putData(Map<Integer, List<OrderItem>> re, OrderItem it, Integer date) {
		if (date != null) {
			List<OrderItem> its = re.get(date);
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
