package user.configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleDoubleEntryException(HttpServletRequest request, Exception e) {
		log.error(e.getMessage(), e);
		return ErrorResponse.builder().title("BAD_REQUEST").detail(e.getMessage())
				.request(request.getMethod() + " " + request.getRequestURI()).time(getTime()).build();
	}

	private String getTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}

	@Value
	@Builder
	public static class ErrorResponse {
		String title;
		String detail;
		String request;
		String time;
	}
}
