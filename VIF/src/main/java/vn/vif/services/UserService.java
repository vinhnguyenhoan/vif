package vn.vif.services;

import vn.vif.models.VIFUser;

public interface UserService extends GeneralService<VIFUser> {
	public VIFUser getLogin();
	public VIFUser findByUsername(String username);
}
