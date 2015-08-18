package vn.vif.services;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import vn.vif.models.Customer;

@Service
public class CustomerServiceImpl extends GeneralServiceImpl<Customer> implements CustomerService {
	
	private static final String LOGIN_KEY = "LOGIN";

	@Override
	public Class<Customer> getEntityClass() {
		return Customer.class;
	}

	@Override
	public void login(Customer cus) {
		RequestContextHolder.currentRequestAttributes().setAttribute(LOGIN_KEY, cus, RequestAttributes.SCOPE_SESSION);
	}

	@Override
	public Customer getLogin() {
		Object cus = RequestContextHolder.currentRequestAttributes().getAttribute(LOGIN_KEY, RequestAttributes.SCOPE_SESSION);
		if (cus != null && cus instanceof Customer) {
			return (Customer) cus;
		}
		return null;
	}

}