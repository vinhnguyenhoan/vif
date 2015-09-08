package vn.vif.services;

import org.springframework.stereotype.Service;

import vn.vif.models.OrderItem;

@Service
public class OrderItemServiceImpl extends GeneralServiceImpl<OrderItem> implements OrderItemService {
	
	@Override
	public Class<OrderItem> getEntityClass() {
		return OrderItem.class;
	}

}