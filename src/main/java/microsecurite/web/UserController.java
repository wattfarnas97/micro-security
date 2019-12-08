package microsecurite.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import microsecurite.entities.AppUser;
import microsecurite.model.UserForm;
import microsecurite.service.AccountService;

@RestController
public class UserController {
	@Autowired
	private AccountService accountService ;
	@PostMapping("/register")
	public AppUser register(@ RequestBody UserForm userForm) {
		
		return accountService.saveUser(userForm.getUsername(), userForm.getPassword(), userForm.getConfirmedPassword());
	}
}
