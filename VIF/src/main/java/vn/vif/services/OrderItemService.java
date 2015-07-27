package vn.vif.services;

import java.util.List;

import vn.vif.models.OrderItem;

public interface OrderItemService extends GeneralService<OrderItem> {

	List<OrderItem> getOrderItemToday();

	List<OrderItem> getOrderItemAllDay();

}
