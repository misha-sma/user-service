package user.exception;

public class EmailException extends RuntimeException {

	private static final long serialVersionUID = 4L;

	public EmailException(String message) {
		super(message);
	}
}
