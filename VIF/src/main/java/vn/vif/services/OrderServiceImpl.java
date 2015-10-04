package vn.vif.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.vif.models.MenuItem;
import vn.vif.models.OrderDetail;
import vn.vif.models.OrderLineDetail;
import vn.vif.models.OrderList;

@Service
public class OrderServiceImpl extends GeneralServiceImpl<OrderList> implements OrderService {

	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private MenuItemService menuItemService;
	
	public Class<OrderList> getEntityClass() {
		return OrderList.class;
	}

	@SuppressWarnings("unchecked")
	public List<OrderLineDetail>[] getOrderListToday() {
		List<MenuItem>[] menuToday = menuItemService.getOrderListToday();
		List<OrderLineDetail> itemNormal = convertFormMenuItemList(menuToday[0]);
		List<OrderLineDetail> itemSpec = convertFormMenuItemList(menuToday[1]);
		return (List<OrderLineDetail>[]) new List[]{itemNormal, itemSpec};
	}

	private List<OrderLineDetail> convertFormMenuItemList(List<MenuItem> menuItems) {
		List<OrderLineDetail> item = new LinkedList<OrderLineDetail>();
		if (menuItems == null) {
			return item;
		}
		int indexNormal = 1;
		for (MenuItem mI : menuItems) {
			OrderDetail detail = new OrderDetail();
			item.add(new OrderLineDetail(indexNormal++, mI.getOrderItem(), detail));
		}
		return item;
	}
	
	@Override
	public OrderList findByCode(String code) {
		List<OrderList> list = list(new String[] {"code"}, new Object[] {code}, true, null, -1, 1);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

}
