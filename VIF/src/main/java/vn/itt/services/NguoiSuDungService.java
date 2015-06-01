package vn.itt.services;

import vn.itt.models.NguoiSuDung;

public interface NguoiSuDungService extends GeneralService<NguoiSuDung> {
	public NguoiSuDung findByUsername(String username);
}
