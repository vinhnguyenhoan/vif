package vn.vif.services;

import org.springframework.stereotype.Service;

import vn.vif.models.Customer;

@Service
public class CustomerServiceImpl extends GeneralServiceImpl<Customer> implements CustomerService {

	@Override
	public Class<Customer> getEntityClass() {
		return Customer.class;
	}

}