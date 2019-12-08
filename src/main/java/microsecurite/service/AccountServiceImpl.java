package microsecurite.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import microsecurite.dao.AppRoleRepository;
import microsecurite.dao.AppUserRepository;
import microsecurite.entities.AppRole;
import microsecurite.entities.AppUser;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
@Autowired
private AppUserRepository appUserRepository;
@Autowired
private AppRoleRepository appRoleRepository;
@Autowired
private BCryptPasswordEncoder bcryptencoder;
    @Override
	public AppUser saveUser(String username, String password, String confirmedPassword) {
		AppUser user=appUserRepository.findByUsername(username);
		if(user!=null) throw new RuntimeException("user already exists");
		System.out.println(password);
		System.out.println(confirmedPassword);
		if(!password.equals(confirmedPassword)) throw new RuntimeException("Please confirm your password");
		AppUser appUser=new AppUser();
		appUser.setUsername(username);
		appUser.setActivated(true);
		appUser.setPassword(bcryptencoder.encode(password));
		appUserRepository.save(appUser);
		addRoleToUser(username, "USER");
		
		return appUser;
    }

	@Override
	public AppRole save(AppRole role) {
		

		return appRoleRepository.save(role);
	}

	@Override
	public AppUser loadUserByUsername(String username) {
		
		return appUserRepository.findByUsername(username);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		AppUser appUser=appUserRepository.findByUsername(username);
		AppRole appRole=appRoleRepository.findByRoleName(rolename); 
		if(appUser !=null & appRole!=null) {
			appUser.getRoles().add(appRole);
		}
		else
		{
			System.out.println("gfyhjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
			throw new RuntimeException("Role ou username incorrect");
		}
		
	}

}
