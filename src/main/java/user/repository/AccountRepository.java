package user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import user.data.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("select a from Account a order by a.id limit 1 offset :offset")
	Optional<Account> getAccountByOffset(int offset);

	Optional<Account> findByUserId(Long userId);
}
