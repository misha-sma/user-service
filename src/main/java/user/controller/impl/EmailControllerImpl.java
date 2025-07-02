package user.controller.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import user.controller.EmailController;
import user.service.EmailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
public class EmailControllerImpl implements EmailController {

	private final EmailService emailService;

	@Override
	@DeleteMapping("")
	@ResponseStatus(HttpStatus.OK)
	public void deleteEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String email) {
		emailService.deleteEmail(token, email);
	}

	@Override
	@PutMapping("")
	@ResponseStatus(HttpStatus.OK)
	public void addEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String email) {
		emailService.addEmail(token, email);
	}

	@Override
	@PatchMapping("")
	@ResponseStatus(HttpStatus.OK)
	public void changeEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String oldEmail,
			@RequestParam String newEmail) {
		emailService.changeEmail(token, oldEmail, newEmail);
	}
}
