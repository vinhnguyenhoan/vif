package vn.vif.services;

import java.util.List;

import vn.vif.models.AddressNote;
import vn.vif.utils.converter.OptionItem;

public interface AddressNoteService extends GeneralService<AddressNote> {

	List<AddressNote> listByDistrictId(Long distId);

	List<OptionItem> listByDistrictIdAsOptionItems(Long districtId);

}
