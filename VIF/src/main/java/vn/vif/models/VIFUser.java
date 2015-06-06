package vn.vif.models;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import vn.vif.daos.LoginRole;

/**
 * NguoiSuDung generated by hbm2java
 */
@Entity
@Table(name = "USER")
public class VIFUser implements java.io.Serializable {
	private static final long serialVersionUID = 3293675195247547203L;

	private long id;
	private String userName;
	private String password;
	private String fullName;
	private Date birthDay;
	private String email;
	private String role;
	private Boolean locked;

	private Long updatedBy;
	private Date updatedDate;
	private Long createdBy;
	private Date createdDate;
	
	public VIFUser() {
	}
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "USER_NAME", unique=true, length = 100)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String maDangNhap) {
		this.userName = maDangNhap;
	}

	@Column(name = "PASSWORD", length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password=password;
	}

	@Column(name = "FULL_NAME", length = 100)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTHDAY", length = 7)
	public Date getBirthday() {
		return this.birthDay;
	}

	public void setBirthday(Date birthday) {
		this.birthDay = birthday;
	}

	@Column(name = "EMAIL", length = 512)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "LOCKED", precision = 1, scale = 0)
	public Boolean isLocked() {
		return this.locked;
	}

	public void setLocked(Boolean isLocked) {
		this.locked = isLocked;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_DATE")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}	

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "UPDATED_BY",precision = 10, scale = 0)
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "CREATED_BY",precision = 10, scale = 0)
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String toString() {
		return String.format("%s[%s]", fullName, userName);
	}

	@Column(name = "ROLE")
	public String getRole() {
		return role;
	}

	public void setRole(String vaiTro) {
		this.role = vaiTro;
	}

	@Transient	
	public Collection<LoginRole> getAuthorities() {
		Collection<LoginRole> set=new HashSet<LoginRole>();
		LoginRole role=new LoginRole();
		role.setRoleName(this.getRole());
		set.add(role);
		return set;
	}	
}