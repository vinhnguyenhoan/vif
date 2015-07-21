package vn.vif.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMER")
public class Customer implements java.io.Serializable {

	private static final long serialVersionUID = -2438072521185484380L;
	
	private Long id;
	
	private Long addressNoteId;

	private String address;
	
	private String name;
	private String email;
	private String phone;
	
	private String note;

	public Customer() {
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

	@Column(name = "ADDRESS_NOTE_ID", precision = 5, scale = 0)
	public Long getAddressNoteId() {
		return addressNoteId;
	}

	public void setAddressNoteId(Long addressNoteId) {
		this.addressNoteId = addressNoteId;
	}
	
	@Column(name = "NAME", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "EMAIL", length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "PHONE", unique = true, nullable = false, length = 20)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "NOTE", length = 200)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "ADDRESS", length = 200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String addressNote) {
		this.address = addressNote;
	}
}