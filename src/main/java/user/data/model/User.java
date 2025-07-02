package user.data.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@JsonIgnore
	@Column(name = "password")
	private String password;

	@OneToMany(mappedBy = "userId")
	private List<EmailData> emails;

	@OneToMany(mappedBy = "userId")
	private List<PhoneData> phones;
}
