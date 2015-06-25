package vn.vif.models.filter;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import vn.vif.daos.Filter;
import vn.vif.models.OrderItem;

public class OrderItemListFilter implements Filter {

	private String searchText;
	
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Criteria getCriteria(Session session) {
		Criteria criteria = session.createCriteria(OrderItem.class);
		
		if (StringUtils.isNotEmpty(searchText)) {
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.ilike("name", "%" + searchText + "%"))
					.add(Restrictions.ilike("desc", "%" + searchText + "%"))
					);
		}

		criteria.addOrder(Order.desc("name"));
		return criteria;
	}

}