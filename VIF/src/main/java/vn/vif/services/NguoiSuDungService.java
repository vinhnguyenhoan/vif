package vn.vif.services;

import vn.vif.models.NguoiSuDung;

public interface NguoiSuDungService extends GeneralService<NguoiSuDung> {
	public NguoiSuDung findByUsername(String username);
}
