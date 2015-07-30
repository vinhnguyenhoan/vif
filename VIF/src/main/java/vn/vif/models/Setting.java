package vn.vif.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SETTING")
public class Setting implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1623890224934717545L;

	private Long id;
	
	private Integer startWeek;
	private String email;
	private String address;
	private String phoneNumber;
	private String companyName;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ADDRESS", length = 100)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "START_WEEK", length = 4)
	public Integer getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(Integer startWeek) {
		this.startWeek = startWeek;
	}

	@Column(name = "EMAIL", length = 30)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "PHONE_NUMBER", length = 20)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "COMPANY_NAME", length = 50)
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
