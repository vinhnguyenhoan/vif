package vn.vif.services;

import org.springframework.stereotype.Service;

import vn.vif.models.OrderDetail;

@Service
public class OrderDetailServiceImpl extends GeneralServiceImpl<OrderDetail> implements OrderDetailService {

	@Override
	public Class<OrderDetail> getEntityClass() {
		return OrderDetail.class;
	}

}
