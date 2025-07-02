package user.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import user.data.dto.DataForUserLoginDto;
import user.data.dto.SuccessfulUserLoginDto;
import user.data.model.Account;
import user.data.model.User;
import user.exception.LoginException;
import user.exception.TransferException;
import user.repository.AccountRepository;
import user.repository.UserRepository;
import user.repository.UserSpecifications;
import user.service.UserService;
import user.util.JwtTokenUtil;
import user.util.Md5Util;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private static final Integer DEFAULT_PAGE_SIZE = 10;
	private static final Integer DEFAULT_PAGE = 1;
	private static final BigDecimal RATE = new BigDecimal(1.1);
	private static final BigDecimal THRESHOLD_RATE = new BigDecimal(2.07);

	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	private final JwtTokenUtil jwtTokenUtil;

	private Map<Long, BigDecimal> startBalanceMap = new HashMap<Long, BigDecimal>();

	@Override
	public SuccessfulUserLoginDto login(DataForUserLoginDto loginInfo) {
		log.info("Login user with email " + loginInfo.email());
		User user = userRepository.getUserByEmail(loginInfo.email())
				.orElseThrow(() -> new LoginException("Wrong login info"));
		String passwordMd5Sum = Md5Util.getMd5Sum(loginInfo.password());
		if (!user.getPassword().equals(passwordMd5Sum)) {
			throw new LoginException("Wrong login info");
		}
		String token = jwtTokenUtil.createAccessToken(user.getId());
		return new SuccessfulUserLoginDto(token);
	}

	@Override
	public List<User> getUsers(LocalDate dateOfBirth, String name, String email, String phone, Integer page,
			Integer pageSize) {
		log.info("Search users with params: dateOfBirth=" + dateOfBirth + " name=" + name + " email=" + email
				+ " phone=" + phone + " page=" + page + " pageSize=" + pageSize);
		if (page == null || page <= 0) {
			page = DEFAULT_PAGE;
		}
		if (pageSize == null || pageSize <= 0) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		List<Specification<User>> specList = new ArrayList<Specification<User>>();
		if (dateOfBirth != null) {
			specList.add(UserSpecifications.hasDateOfBirth(dateOfBirth));
		}
		if (name != null && !name.isBlank()) {
			specList.add(UserSpecifications.hasName(name.trim()));
		}
		if (email != null && !email.isBlank()) {
			specList.add(UserSpecifications.hasEmail(email));
		}
		if (phone != null && !phone.isBlank()) {
			specList.add(UserSpecifications.hasPhone(phone));
		}
		Specification<User> spec = Specification.allOf(specList);
		Pageable p = PageRequest.of(page - 1, pageSize, Sort.by("id"));
		return userRepository.findAll(spec, p).getContent();
	}

	@Override
	@Transactional
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public void transfer(String token, Long userIdTo, BigDecimal value) {
		log.info("Transfer to user with id=" + userIdTo + " value=" + value);
		value = value.setScale(2, RoundingMode.HALF_UP);
		if (value.compareTo(BigDecimal.ZERO) <= 0) {
			throw new TransferException("Value <= 0");
		}
		Long userIdFrom = jwtTokenUtil.getUserId(token);
		if (userIdFrom.equals(userIdTo)) {
			return;
		}
		Account accountFrom = accountRepository.findByUserId(userIdFrom)
				.orElseThrow(() -> new TransferException("AccountFrom not found, userId=" + userIdFrom));
		if (accountFrom.getBalance().compareTo(value) < 0) {
			throw new TransferException("Not enough money");
		}
		Account accountTo = accountRepository.findByUserId(userIdTo)
				.orElseThrow(() -> new TransferException("AccountTo not found, userId=" + userIdTo));
		BigDecimal balanceFromNew = accountFrom.getBalance().subtract(value).setScale(2, RoundingMode.HALF_UP);
		BigDecimal balanceToNew = accountTo.getBalance().add(value).setScale(2, RoundingMode.HALF_UP);
		accountFrom.setBalance(balanceFromNew);
		accountRepository.save(accountFrom);
		accountTo.setBalance(balanceToNew);
		accountRepository.save(accountTo);
	}

	@Transactional
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	private boolean increaseBalance(int offset) {
		Optional<Account> accountOptional = accountRepository.getAccountByOffset(offset);
		if (accountOptional.isEmpty()) {
			return false;
		}
		Account account = accountOptional.get();
		BigDecimal startBalance = startBalanceMap.get(account.getId());
		if (startBalance == null) {
			startBalanceMap.put(account.getId(), account.getBalance());
			return true;
		}
		if (account.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
			return true;
		}
		BigDecimal newBalance = account.getBalance().multiply(RATE).setScale(2, RoundingMode.HALF_UP);
		BigDecimal maxBalance = startBalance.multiply(THRESHOLD_RATE).setScale(2, RoundingMode.HALF_UP);
		if (newBalance.compareTo(maxBalance) > 0) {
			return true;
		}
		account.setBalance(newBalance);
		accountRepository.save(account);
		return true;
	}

	@Scheduled(fixedDelay = 30000)
	public void computePrice() {
		log.info("Scheduler started");
		int offset = 0;
		boolean isContinue = true;
		do {
			isContinue = increaseBalance(offset);
			++offset;
		} while (isContinue);
		log.info("Scheduler finished");
	}
}
