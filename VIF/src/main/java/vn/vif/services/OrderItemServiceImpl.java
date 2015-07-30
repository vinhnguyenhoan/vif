package vn.vif.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.stereotype.Service;

import vn.vif.models.OrderItem;
import vn.vif.models.filter.OrderItemListFilter;

@Service
public class OrderItemServiceImpl extends GeneralServiceImpl<OrderItem> implements OrderItemService {

	@Override
	public Class<OrderItem> getEntityClass() {
		return OrderItem.class;
	}

	@Override
	public List<OrderItem> getOrderItemToday() {
		int dayOfW = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK);
		OrderItemListFilter itemFilter = new OrderItemListFilter();
		itemFilter.setSearchDate(Arrays.asList(dayOfW));
		itemFilter.setSpecItem(false);
		return list(itemFilter);
	}

	@Override
	public List<OrderItem> getOrderItemAllDay() {
		OrderItemListFilter itemFilter = new OrderItemListFilter();
		itemFilter.setSpecItem(true);
		return list(itemFilter);
	}
}
