package vn.vif.services;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import vn.vif.daos.Filter;
import vn.vif.models.AddressNote;

@Service
public class AddressNoteServiceImpl extends GeneralServiceImpl<AddressNote> implements AddressNoteService {

	private final class AddressNoteFilterByDistId implements Filter {

		private Long searchDistrictId;

		@Override
		public Criteria getCriteria(Session session) {
			Criteria criteria = session.createCriteria(AddressNote.class);
			
			if (searchDistrictId != null) {
				Disjunction districtDis = Restrictions.disjunction();
				districtDis.add(Restrictions.eq("districtId", searchDistrictId));
				criteria.add(districtDis);
			}
			
			criteria.addOrder(Order.desc("districtId"))
					.addOrder(Order.asc("street"))
					.addOrder(Order.asc("address"))
					.addOrder(Order.asc("officeName"))
					.addOrder(Order.asc("level"));
			return criteria;
		}
	};
	
	private final AddressNoteFilterByDistId addressNoteFilterByDistId = new AddressNoteFilterByDistId();
	
	@Override
	public Class<AddressNote> getEntityClass() {
		return AddressNote.class;
	}

	@Override
	public List<AddressNote> listByDistrictId(Long distId) {
		addressNoteFilterByDistId.searchDistrictId = distId;
		return list(addressNoteFilterByDistId);
	}

}