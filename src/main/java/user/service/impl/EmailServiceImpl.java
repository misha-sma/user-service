package user.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import user.data.model.EmailData;
import user.exception.EmailException;
import user.repository.EmailDataRepository;
import user.service.EmailService;
import user.util.JwtTokenUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final EmailDataRepository emailDataRepository;
	private final JwtTokenUtil jwtTokenUtil;

	@Override
	@Transactional
	public void deleteEmail(String token, String email) {
		log.info("Delete email " + email);
		Long userId = jwtTokenUtil.getUserId(token);
		Integer emailsCount = emailDataRepository.getEmailsCount(userId);
		if (emailsCount <= 1) {
			throw new EmailException("Can't delete single email " + email);
		}
		emailDataRepository.deleteEmail(userId, email);
	}

	@Override
	public void addEmail(String token, String email) {
		log.info("Add email " + email);
		Long userId = jwtTokenUtil.getUserId(token);
		Optional<EmailData> optional = emailDataRepository.findByEmail(email);
		if (optional.isPresent()) {
			throw new EmailException("Email " + email + " already exists");
		}
		emailDataRepository.save(new EmailData(null, userId, email));
	}

	@Override
	public void changeEmail(String token, String oldEmail, String newEmail) {
		log.info("Change email: oldEmail=" + oldEmail + " newEmail=" + newEmail);
		if (oldEmail.equals(newEmail)) {
			return;
		}
		Long userId = jwtTokenUtil.getUserId(token);
		Optional<EmailData> newEmailOptional = emailDataRepository.findByEmail(newEmail);
		if (newEmailOptional.isPresent()) {
			throw new EmailException("Email " + newEmail + " already exists");
		}
		EmailData emailData = emailDataRepository.findByUserIdAndEmail(userId, oldEmail)
				.orElseThrow(() -> new EmailException("Email " + oldEmail + " doesn't exist"));
		emailData.setEmail(newEmail);
		emailDataRepository.save(emailData);
	}
}
