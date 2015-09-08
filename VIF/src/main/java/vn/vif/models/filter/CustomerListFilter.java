package vn.vif.models.filter;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import vn.vif.daos.DistinctFilter;
import vn.vif.models.Customer;

public class CustomerListFilter implements DistinctFilter {

	private String searchText;
	private boolean searchNotActive;
//	private List<Long> searchDistrict;
	
	public String getSearchText() {
		return searchText;
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
		Criteria criteria = session.createCriteria(Customer.class);
		
		if (StringUtils.isNotEmpty(searchText)) {
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.ilike("name", "%" + searchText + "%"))
					.add(Restrictions.ilike("email", "%" + searchText + "%"))
					.add(Restrictions.ilike("phone", "%" + searchText + "%"))
					);
		}

//		if (searchDistrict != null) {
//			Disjunction districtDis = Restrictions.disjunction();
//			districtDis.add(Restrictions.in("districtId", searchDistrict));
//			criteria.add(districtDis);
//		}
		
		if (searchNotActive) {
			Disjunction searchNotActiveDis = Restrictions.disjunction();
			//searchNotActiveDis.add(Restrictions.eq("active", searchActive));
			searchNotActiveDis.add(Restrictions.or(
					Restrictions.eq("active", false),
					Restrictions.isNull("active")));
			criteria.add(searchNotActiveDis);
		} /*else {
			searchActiveDis.add(Restrictions.or(
					Restrictions.eq("active", false),
					Restrictions.isNull("active")));
		}*/
		
		criteria.addOrder(Order.desc("name"));
		return criteria;
	}

	public boolean getSearchNotActive() {
		return searchNotActive;
	}

	public void setSearchNotActive(boolean searchNotActive) {
		this.searchNotActive = searchNotActive;
	}

	@Override
	public String getId() {
		return "id";
	}
}
