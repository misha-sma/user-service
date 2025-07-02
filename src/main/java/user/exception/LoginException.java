package user.exception;

public class LoginException extends RuntimeException {

	private static final long serialVersionUID = 3L;

	public LoginException(String message) {
		super(message);
	}
}
