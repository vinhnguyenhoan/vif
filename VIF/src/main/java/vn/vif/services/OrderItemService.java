package vn.vif.services;

import java.util.List;
import java.util.Map;

import vn.vif.models.OrderItem;

public interface OrderItemService extends GeneralService<OrderItem> {

	List<OrderItem> getOrderItemToday();

	List<OrderItem> getOrderItemAllDay();

	public Map<Integer, List<OrderItem>>[] getOrderItemData();

}
