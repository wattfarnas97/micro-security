package microsecurite.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import microsecurite.entities.AppUser;
@RepositoryRestResource
public interface AppUserRepository  extends JpaRepository<AppUser, Long>{
public AppUser findByUsername(String username); 
}
