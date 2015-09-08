package vn.vif.services;

import java.util.List;

import vn.vif.models.OrderLineDetail;
import vn.vif.models.OrderList;

public interface OrderService extends GeneralService<OrderList> {

	List<OrderLineDetail> getOrderListToday();

	List<OrderLineDetail> getOrderListAllDay();

	OrderList findByCode(String code);

}
