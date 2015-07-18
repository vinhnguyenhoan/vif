package vn.vif.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Order implements java.io.Serializable {

	private static final long serialVersionUID = -1268094244606364985L;

	private Long id;

	private Long customerId;

	private String note;
	
	public Order() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CUSTOMER_ID", precision = 10, scale = 0)
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@Column(name = "NOTE", length = 200)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
