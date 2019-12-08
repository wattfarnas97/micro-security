package microsecurite;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import microsecurite.entities.AppRole;
import microsecurite.service.AccountService;

@SpringBootApplication
public class MicroSecuriteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroSecuriteApplication.class, args);
	}
	@Bean
	CommandLineRunner start(AccountService accountService) {
		return args->{
			accountService.save(new AppRole(null, "USER"));
			accountService.save(new AppRole(null, "ADMIN"));
			Stream.of("user","admin").forEach(u->{
				accountService.saveUser(u,"1234", "1234");
			});
			accountService.addRoleToUser("admin", "ADMIN");
		};
	}
	@Bean
	BCryptPasswordEncoder getBcrypt() {
		return new BCryptPasswordEncoder();
	}

}
