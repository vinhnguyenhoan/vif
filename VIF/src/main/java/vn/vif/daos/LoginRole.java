package vn.vif.daos;

import org.springframework.security.core.GrantedAuthority;


public class LoginRole implements GrantedAuthority{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3608506588100788903L;
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getAuthority() {		
		return roleName;
	}	

}
