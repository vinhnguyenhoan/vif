package vn.vif.services;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import vn.vif.daos.Filter;
import vn.vif.models.AddressNote;
import vn.vif.utils.VIFUtils;
import vn.vif.utils.converter.OptionItem;

@Service
public class AddressNoteServiceImpl extends GeneralServiceImpl<AddressNote> implements AddressNoteService {

	private final class AddressNoteFilterByDistId implements Filter {

		private Long searchDistrictId;

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

	public List<AddressNote> listByDistrictId(Long distId) {
		if (!VIFUtils.isValid(distId)) {
			return new LinkedList<AddressNote>();
		}
		addressNoteFilterByDistId.searchDistrictId = distId;
		return list(addressNoteFilterByDistId);
	}

	public List<OptionItem> listByDistrictIdAsOptionItems(Long districtId) {
		List<AddressNote> listANFromDis = listByDistrictId(districtId);
		List<OptionItem> listOI = new LinkedList<OptionItem>();
		for (AddressNote aN : listANFromDis) {
			listOI.add(new OptionItem(aN.getId(), aN.toString()));
		}
		return listOI;
	}

}