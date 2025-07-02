package user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import user.data.model.EmailData;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {

	@Modifying
	@Query("delete from EmailData e where e.userId=:userId and e.email=:email")
	void deleteEmail(Long userId, String email);

	Optional<EmailData> findByEmail(String email);

	Optional<EmailData> findByUserIdAndEmail(Long userId, String email);
	
	@Query("select count(e) from EmailData e where e.userId=:userId")
	Integer getEmailsCount(Long userId);
}
