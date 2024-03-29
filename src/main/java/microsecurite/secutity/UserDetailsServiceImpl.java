package microsecurite.secutity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import microsecurite.entities.AppUser;
import microsecurite.service.AccountService;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
	private AccountService accountService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser=accountService.loadUserByUsername(username);
		if(appUser==null) throw new UsernameNotFoundException("Invalid User");
		 Collection<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
		appUser.getRoles().forEach(r->{
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
		});
		 return new User(appUser.getUsername(),appUser.getPassword(),authorities);
	}

}
