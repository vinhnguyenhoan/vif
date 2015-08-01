package vn.vif.models.filter;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import vn.vif.daos.DistinctFilter;
import vn.vif.models.OrderList;

public class OrderListFilter implements DistinctFilter {
	private String searchText;
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
		
		criteria.addOrder(Order.desc("id"));
		return criteria;
	}

	@Override
	public String getId() {
		return "id";
	}
}
