package vn.vif.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS_NOTE")
public class AddressNote implements java.io.Serializable {
	
	private static final long serialVersionUID = -5113139838406217007L;

	private Long id;
	
	private Long provinceId;
	private String street;
	private String address;
	private String officeName;
	private String officeLevel;

	public AddressNote() {
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

	@Column(name = "PROVINCE_ID", precision = 2, scale = 0)
	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	@Column(name = "STREET", length = 50)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(name = "ADDRESS", length = 50)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "OFFICE_NAME", length = 70)
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String office) {
		this.officeName = office;
	}

	@Column(name = "OFFICE_LEVEL", length = 50)
	public String getLevel() {
		return officeLevel;
	}

	public void setLevel(String level) {
		this.officeLevel = level;
	}
}
