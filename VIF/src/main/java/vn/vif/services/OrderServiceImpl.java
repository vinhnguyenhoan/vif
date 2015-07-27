package vn.vif.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.vif.models.OrderDetail;
import vn.vif.models.OrderItem;
import vn.vif.models.OrderLineDetail;
import vn.vif.models.OrderList;

@Service
public class OrderServiceImpl extends GeneralServiceImpl<OrderList> implements OrderService {

	@Autowired
	private OrderItemService orderItemService;
	
	@Override
	public Class<OrderList> getEntityClass() {
		return OrderList.class;
	}

	@Override
	public List<OrderLineDetail> getOrderListToday() {
		List<OrderLineDetail> result = new LinkedList<>();
		List<OrderItem> orderItemToday = orderItemService.getOrderItemToday();
		int index = 1;
		for (OrderItem oI : orderItemToday) {
			OrderDetail detail = new OrderDetail();
			result.add(new OrderLineDetail(index++, oI, detail));
		}
		return result;
	}

	@Override
	public List<OrderLineDetail> getOrderListAllDay() {
		List<OrderLineDetail> result = new LinkedList<>();
		List<OrderItem> orderItemAllDay = orderItemService.getOrderItemAllDay();
		int index = 1;
		for (OrderItem oI : orderItemAllDay) {
			OrderDetail detail = new OrderDetail();
			result.add(new OrderLineDetail(index++, oI, detail));
		}
		return result;
	}

}
