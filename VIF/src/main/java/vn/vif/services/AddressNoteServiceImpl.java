package vn.vif.services;

import org.springframework.stereotype.Service;

import vn.vif.models.AddressNote;

@Service
public class AddressNoteServiceImpl extends GeneralServiceImpl<AddressNote> implements AddressNoteService {

	@Override
	public Class<AddressNote> getEntityClass() {
		return AddressNote.class;
	}

}