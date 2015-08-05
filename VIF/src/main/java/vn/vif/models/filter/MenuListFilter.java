package vn.vif.models.filter;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import vn.vif.daos.Filter;
import vn.vif.models.MenuItem;
import vn.vif.utils.VIFUtils;

public class MenuListFilter implements Filter {

	private String searchText;
	private Long addItem;
	private Long deleteItem;
	private Integer week;
	private Integer day;

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Long getAddItem() {
		return addItem;
	}

	public void setAddItem(Long addItem) {
		this.addItem = addItem;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Long getDeleteItem() {
		return deleteItem;
	}

	public void setDeleteItem(Long deleteItem) {
		this.deleteItem = deleteItem;
	}

	public Criteria getCriteria(Session session) {
		Criteria criteria = session.createCriteria(MenuItem.class);
		Criteria orderItemCriteria = criteria.createCriteria("orderItem");
		
		if (StringUtils.isNotEmpty(searchText)) {
			orderItemCriteria.add(Restrictions.disjunction()
					.add(Restrictions.ilike("name", "%" + searchText + "%"))
					.add(Restrictions.ilike("desc", "%" + searchText + "%"))
					);
		}

		if (VIFUtils.isValid(week)) {
			criteria.add(Restrictions.eq("week", week));
		}
		
		if (VIFUtils.isValid(day)) {
			criteria.add(Restrictions.eq("day", day));
		}
		
		criteria.addOrder(Order.desc("createdDate"));
		
		return criteria;
	}

}
