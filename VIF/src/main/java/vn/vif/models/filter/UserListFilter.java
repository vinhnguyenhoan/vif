package vn.vif.models.filter;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import vn.vif.daos.Filter;
import vn.vif.models.VIFUser;
import vn.vif.utils.VIFUtils;

public class UserListFilter implements Filter {

	private String searchText;
	private boolean locked;
	
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Criteria getCriteria(Session session) {
		Criteria criteria = session.createCriteria(VIFUser.class);
		if (isLocked()) {
			criteria.add(Restrictions.eq("locked", isLocked()));
		}
		
		if (VIFUtils.isValid(searchText)) {
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.ilike("fullName", "%" + searchText + "%"))
					.add(Restrictions.ilike("userName", "%" + searchText + "%"))
					);
		}

		criteria.addOrder(Order.desc("fullName"));
		return criteria;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
}
