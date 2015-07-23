package vn.vif.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "CUSTOMER")
public class Customer implements java.io.Serializable {

	private static final long serialVersionUID = -2438072521185484380L;
	
	private Long id;
	
	@Embedded
	private AddressNote addressNote;
	@Transient
	private Long addressNoteId;
	@Transient
	private Long districtId;
	
	private String address;
	
	private String name;
	private String email;
	private String phone;
	
	private String note;

	private Boolean active;
	
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

	@Column(name = "NAME", length = 100, nullable = false)
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

	@Transient
	public String getAddressFull() {
		String result = address;
		if (addressNote != null) {
			result = address + " ( " + addressNote + " )"; 
		}
		return result;
	}
	
	public void setAddress(String addressNote) {
		this.address = addressNote;
	}

	@Column(name = "ACTIVE")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean isActive) {
		this.active = isActive;
	}
	
	@Transient
	public String getStatusText() {
		if (active == null || !active) {
			return "Chưa kích hoạt";
		}
		return "Đã kích hoạt";
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "ADDRESS_NOTE_ID")
	public AddressNote getAddressNote() {
		return this.addressNote;
	}

	public void setAddressNote(AddressNote addressNote) {
		this.addressNote = addressNote;
	}

	public Long getDistrictId() {
		if (addressNote != null) {
			return addressNote.getDistrictId();
		}
		return -1l;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public Long getAddressNoteId() {
		if (addressNote != null) {
			return addressNote.getId();
		}
		return -1l;
	}

	public void setAddressNoteId(Long addressNoteId) {
		this.addressNoteId = addressNoteId;
	}
	
}