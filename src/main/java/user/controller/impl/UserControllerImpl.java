package user.controller.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import user.controller.UserController;
import user.data.dto.DataForUserLoginDto;
import user.data.dto.SuccessfulUserLoginDto;
import user.data.model.User;
import user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserControllerImpl implements UserController {

	private final UserService userService;

	@Override
	@PostMapping("/login")
	public SuccessfulUserLoginDto login(@RequestBody DataForUserLoginDto loginInfo) {
		return userService.login(loginInfo);
	}

	@Override
	@GetMapping("")
	public List<User> getUsers(@RequestParam(required = false) LocalDate dateOfBirth,
			@RequestParam(required = false) String name, @RequestParam(required = false) String email,
			@RequestParam(required = false) String phone, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		return userService.getUsers(dateOfBirth, name, email, phone, page, pageSize);
	}

	@Override
	@PatchMapping("/transfer")
	@ResponseStatus(HttpStatus.OK)
	public void transfer(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam Long userIdTo,
			@RequestParam BigDecimal value) {
		userService.transfer(token, userIdTo, value);
	}
}
