package vn.vif.daos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.vif.models.VIFUser;
import vn.vif.services.UserService;


@Transactional(readOnly=true)
@Service("LoginUserDetailsService")
public class LoginUserDetailsService implements UserDetailsService{
	@Autowired
	private UserService userService;
	
	public UserDetails loadUserByUsername(String username)
	        throws UsernameNotFoundException {
		VIFUser loggedInUser = userService.findByUsername(username);				
	    if(loggedInUser==null) {throw new UsernameNotFoundException("No such user: " + username);
	    } else if (loggedInUser.getAuthorities().isEmpty()) {
	        throw new UsernameNotFoundException("User " + username + " has no authorities");
	    }
	    boolean accountNonExpired = true;
	    boolean credentialsNonExpired = true;	   
	    SecurityUser user=new SecurityUser(loggedInUser.getUserName(),
	    		loggedInUser.getPassword().toLowerCase(),
	    		!loggedInUser.getLocked(),
	            accountNonExpired,
	            credentialsNonExpired,
	            !loggedInUser.getLocked(),
	            getAuthorities(loggedInUser.getAuthorities()));
	    user.setUser(loggedInUser);	  
	    return user;
   }

	public List<String> getRolesAsList(Collection<? extends GrantedAuthority> roles) {
	    List <String> rolesAsList = new ArrayList<String>();
	    for(GrantedAuthority role : roles){
	        rolesAsList.add(role.getAuthority());
	    }
	    return rolesAsList;
	}

	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
	    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	    for (String role : roles) {
	        authorities.add(new SimpleGrantedAuthority(role));
	    }
	    return authorities;
	}

	public Collection<GrantedAuthority> getAuthorities(Collection<LoginRole> roles) {
	    List<GrantedAuthority> authList = getGrantedAuthorities(getRolesAsList(roles));
	    return authList;
	}

}
