package vn.vif.models.filter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import vn.vif.daos.Filter;
import vn.vif.models.OrderItem;

public class OrderItemListFilter implements Filter {

	private String searchText;
	private List<Integer> searchDate;
	private List<OrderItem> itemSelected;
	private List<Integer> moveToDate;
	
	public List<Integer> getMoveToDate() {
		return moveToDate;
	}

	public void setMoveToDate(List<Integer> moveToDate) {
		this.moveToDate = moveToDate;
	}

	public List<OrderItem> getItemSelected() {
		return itemSelected;
	}

	public void setItemSelected(List<OrderItem> itemSelected) {
		this.itemSelected = itemSelected;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public List<Integer> getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(List<Integer> searchDate) {
		this.searchDate = searchDate;
	}

	public Criteria getCriteria(Session session) {
		Criteria criteria = session.createCriteria(OrderItem.class);
		
		if (StringUtils.isNotEmpty(searchText)) {
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.ilike("name", "%" + searchText + "%"))
					.add(Restrictions.ilike("desc", "%" + searchText + "%"))
					);
		}

		if (searchDate != null) {
			Disjunction dateDis = Restrictions.disjunction();
			for (Integer date : searchDate) {
				String colName = OrderItem.getDateColName(date);
				if (colName != null) {
					dateDis.add(Restrictions.eq(colName, date));
				}
			}
			criteria.add(dateDis);
		}
		criteria.addOrder(Order.desc("name"));
		return criteria;
	}

}
