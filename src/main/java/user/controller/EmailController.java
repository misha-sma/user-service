package user.controller;

public interface EmailController {

	void deleteEmail(String token, String email);

	void addEmail(String token, String email);

	void changeEmail(String token, String oldEmail, String newEmail);
}
