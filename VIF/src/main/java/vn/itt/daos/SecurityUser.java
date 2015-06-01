package vn.itt.daos;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import vn.itt.models.NguoiSuDung;

public class SecurityUser extends org.springframework.security.core.userdetails.User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3908817079906409363L;
	
	private NguoiSuDung user;
  

    public NguoiSuDung getUser() {
		return user;
	}


	public void setUser(NguoiSuDung user) {
		this.user = user;
	}


	public SecurityUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities) throws IllegalArgumentException {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
