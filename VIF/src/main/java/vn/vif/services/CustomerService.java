package vn.vif.services;

import vn.vif.models.Customer;

public interface CustomerService extends GeneralService<Customer> {

	void login(Customer cus);
	
	Customer getLogin();

}
