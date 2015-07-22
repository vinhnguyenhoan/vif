package vn.vif.models.filter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import vn.vif.daos.Filter;
import vn.vif.models.AddressNote;

public class AddressNoteListFilter implements Filter {

	private String searchText;
	private List<Long> searchDistrict;
	
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public List<Long> getSearchDistrict() {
		return searchDistrict;
	}

	public void setSearchDistrict(List<Long> searchDistrict) {
		this.searchDistrict = searchDistrict;
	}

	@Override
	public Criteria getCriteria(Session session) {
		Criteria criteria = session.createCriteria(AddressNote.class);
		
		if (StringUtils.isNotEmpty(searchText)) {
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.ilike("street", "%" + searchText + "%"))
					.add(Restrictions.ilike("officeName", "%" + searchText + "%"))
					);
		}

		if (searchDistrict != null) {
			Disjunction districtDis = Restrictions.disjunction();
			districtDis.add(Restrictions.in("districtId", searchDistrict));
			criteria.add(districtDis);
		}
		
		criteria.addOrder(Order.desc("districtId"))
				.addOrder(Order.asc("street"))
				.addOrder(Order.asc("address"))
				.addOrder(Order.asc("officeName"))
				.addOrder(Order.asc("officeLevel"));
		return criteria;
	}
}
