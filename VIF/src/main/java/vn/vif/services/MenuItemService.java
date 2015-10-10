package vn.vif.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import vn.vif.models.MenuItem;

public interface MenuItemService extends GeneralService<MenuItem> {

	public Map<Integer, List<MenuItem>>[] getMenuItemData();
	public List<MenuItem>[] getOrderListToday();
	public List<MenuItem> getOrderList(Date date);
	
}
