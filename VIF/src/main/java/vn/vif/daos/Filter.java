package vn.vif.daos;

import org.hibernate.Criteria;
import org.hibernate.Session;

public interface Filter {
	public Criteria getCriteria(Session session );
}
