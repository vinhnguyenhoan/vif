package vn.vif.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import vn.vif.utils.VIFException;

@Entity
@Table(name = "ADDRESS_NOTE")
public class AddressNote implements java.io.Serializable {
	
	private static final long serialVersionUID = -5113139838406217007L;

	private Long id;
	
	private Long districtId;
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

	@Column(name = "DISTRICT_ID", precision = 2, scale = 0)
	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
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
	
	@Transient
	public String getDistrict() {
		return District.getById(this.districtId).fullName;
	}
	
	@Override
	public String toString() {
		String result = "";
		
		// insert address
		if (!StringUtils.isBlank(address)) {
			result = address;
		}
		// insert street
		if (StringUtils.isEmpty(result)) {
			result = street;
		} else {
			result += ", " + street;
		}
		// insert office name
		if (!StringUtils.isBlank(officeName)) {
			final boolean isFirst = StringUtils.isEmpty(result);
			result += officeName;
			if (!StringUtils.isBlank(officeLevel)) {
				result += " - Tầng " + officeLevel;
			}
			if (!isFirst) {
				result = ", " + result;
			}
		}
		// insert district
		District district = District.getById(districtId);
		if (district == null) {
			throw new VIFException("District Id is invalid " + districtId);
		}
		result += ", " + district.fullName;
		// TODO ward ?
		return result;
	}
}
