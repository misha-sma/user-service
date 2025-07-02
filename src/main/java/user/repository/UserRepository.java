package user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import user.data.model.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	@Query("select u from User u where u.id=(select e.userId from EmailData e where e.email=:email)")
	Optional<User> getUserByEmail(String email);
}
