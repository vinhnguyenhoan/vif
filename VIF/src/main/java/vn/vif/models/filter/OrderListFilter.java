package vn.vif.models.filter;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.StandardBasicTypes;

import vn.vif.daos.DistinctFilter;
import vn.vif.models.OrderList;
import vn.vif.utils.VIFUtils;

public class OrderListFilter implements DistinctFilter {
	private String searchText;
	private Date fromOrderDate;
	private Date toOrderDate;

//	private List<Long> searchDistrict;
	
	public String getSearchText() {
		return searchText;
	}

	public Date getFromDateValue() {
		if (this.fromOrderDate == null) {
			this.fromOrderDate = VIFUtils.convertStringToDate(
					VIFUtils.formatDate(new Date()), "dd/MM/yyyy");// truncate date
		}
		return fromOrderDate;
	}
	
	public String getFromOrderDate() {
		if (this.fromOrderDate != null) {
			return VIFUtils.formatDate(this.fromOrderDate);// truncate date
		}
		return VIFUtils.formatDate(new Date());
	}

	public void setFromOrderDate(String date) {
		this.fromOrderDate = VIFUtils.convertStringToDate(date, "dd/MM/yyyy");
	}
	
	public Date getToDateValue() {
		if (this.toOrderDate == null) {
			this.toOrderDate = VIFUtils.convertStringToDate(
					VIFUtils.formatDate(new Date()), "dd/MM/yyyy");// truncate date
		}
		return toOrderDate;
	}
	
	public String getToOrderDate() {
		if (this.toOrderDate != null) {
			return VIFUtils.formatDate(this.toOrderDate);// truncate date
		}
		return VIFUtils.formatDate(new Date());
	}

	public void setToOrderDate(String date) {
		this.toOrderDate = VIFUtils.convertStringToDate(date, "dd/MM/yyyy");
	}
	
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

//	public List<Long> getSearchDistrict() {
//		return searchDistrict;
//	}
//
//	public void setSearchDistrict(List<Long> searchDistrict) {
//		this.searchDistrict = searchDistrict;
//	}

	public Criteria getCriteria(Session session) {
		Criteria criteria = session.createCriteria(OrderList.class);
		
		if (StringUtils.isNotEmpty(searchText)) {
			Criteria customerCriteria = criteria.createCriteria("customer");
			customerCriteria.add(Restrictions.like("name", "%" + searchText + "%"));
		}

		criteria.add(Restrictions.sqlRestriction(" date(ordered_date) >= date(?) and date(ordered_date) <= date(?)", 
								new Object[]{getFromDateValue(), getToDateValue()}, 
								new DateType[]{StandardBasicTypes.DATE, StandardBasicTypes.DATE}));
		
		criteria.addOrder(Order.desc("id"));
		return criteria;
	}

	@Override
	public String getId() {
		return "id";
	}
}
