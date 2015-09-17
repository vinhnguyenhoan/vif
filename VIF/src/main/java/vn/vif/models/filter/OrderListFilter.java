package vn.vif.models.filter;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import vn.vif.daos.DistinctFilter;
import vn.vif.models.OrderList;
import vn.vif.utils.VIFUtils;

public class OrderListFilter implements DistinctFilter {
	private String searchText;
	private Date date;
//	private List<Long> searchDistrict;
	
	public String getSearchText() {
		return searchText;
	}

	public Date getDateValue() {
		if (this.date == null) {
			this.date = VIFUtils.convertStringToDate(
					VIFUtils.formatDate(new Date()), "dd/MM/yyyy");// truncate date
		}
		return date;
	}
	
	public String getDate() {
		if (this.date != null) {
			return VIFUtils.formatDate(this.date);// truncate date
		}
		return VIFUtils.formatDate(new Date());
	}

	public void setDate(String date) {
		this.date = VIFUtils.convertStringToDate(date, "dd/MM/yyyy");
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
		
//		if (StringUtils.isNotEmpty(searchText)) {
//			criteria.add(Restrictions.disjunction()
//					.add(Restrictions.ilike("customer.name", "%" + searchText + "%"))
//					);
//		}

//		if (searchDistrict != null) {
//			Disjunction districtDis = Restrictions.disjunction();
//			districtDis.add(Restrictions.in("districtId", searchDistrict));
//			criteria.add(districtDis);
//		}
		
		criteria.add(Restrictions.sqlRestriction(" date(ordered_date) = date(?) ", getDateValue(), StandardBasicTypes.DATE));
		
		criteria.addOrder(Order.desc("id"));
		return criteria;
	}

	@Override
	public String getId() {
		return "id";
	}
}
