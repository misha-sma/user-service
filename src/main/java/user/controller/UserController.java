package user.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import user.data.dto.DataForUserLoginDto;
import user.data.dto.SuccessfulUserLoginDto;
import user.data.model.User;

public interface UserController {

	SuccessfulUserLoginDto login(DataForUserLoginDto loginInfo);

	List<User> getUsers(LocalDate dateOfBirth, String name, String email, String phone, Integer page, Integer pageSize);

	void transfer(String token, Long userIdTo, BigDecimal value);
}
