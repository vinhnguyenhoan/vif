package vn.vif.services;

import java.util.List;

import vn.vif.models.AddressNote;

public interface AddressNoteService extends GeneralService<AddressNote> {

	List<AddressNote> listByDistrictId(Long distId);

}
