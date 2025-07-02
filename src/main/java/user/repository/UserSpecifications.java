package user.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import user.data.model.EmailData;
import user.data.model.PhoneData;
import user.data.model.User;

public class UserSpecifications {

	public static Specification<User> hasDateOfBirth(LocalDate dateOfBirth) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("dateOfBirth"), dateOfBirth);
	}

	public static Specification<User> hasName(String name) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), name + "%");
	}

	public static Specification<User> hasEmail(String email) {
		return (root, query, criteriaBuilder) -> {
			Join<User, EmailData> emails = root.join("emails");
			return criteriaBuilder.equal(emails.get("email"), email);
		};
	}

	public static Specification<User> hasPhone(String phone) {
		return (root, query, criteriaBuilder) -> {
			Join<User, PhoneData> phones = root.join("phones");
			return criteriaBuilder.equal(phones.get("phone"), phone);
		};
	}
}
