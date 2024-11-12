package dmu.cheek.emailVerification.service;

import dmu.cheek.emailVerification.converter.EmailVerificationConverter;
import dmu.cheek.emailVerification.model.EmailVerification;
import dmu.cheek.emailVerification.model.EmailVerificationDto;
import dmu.cheek.emailVerification.repository.DomainRepository;
import dmu.cheek.emailVerification.repository.EmailVerificationRepository;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.error.exception.InProgressException;
import dmu.cheek.member.model.Domain;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailVerificationConverter emailVerificationConverter;
    private final JavaMailSender javaMailSender;
    private final ResourceLoader resourceLoader;
    private final DomainRepository domainRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Transactional
    public void registerEmailVerification(EmailVerificationDto emailVerificationDto) throws MessagingException {

        String verificationCode = createVerificationCode();
        emailVerificationDto.setup(verificationCode);

        sendEmailVerification(emailVerificationDto);

        EmailVerification emailVerification = emailVerificationConverter.convertToEntity(emailVerificationDto);
        emailVerificationRepository.save(emailVerification);

        log.info("save emailVerification: {}", emailVerification.getEmail());
    }

    private String createVerificationCode() {
        return String.valueOf((int) (Math.random() * (90000)) + 100000);
    }

    private void sendEmailVerification(EmailVerificationDto emailVerificationDto) throws MessagingException {
        MimeMessage message = createEmailForm(emailVerificationDto);
        javaMailSender.send(message);

        log.info("email sending to {} ", emailVerificationDto.getEmail());
    }

    private MimeMessage createEmailForm(EmailVerificationDto emailVerificationDto) throws MessagingException {
        isEmailDuplicated(emailVerificationDto.getEmail());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            helper.setFrom(new InternetAddress(senderEmail, "CHEEK"));
            helper.setTo(emailVerificationDto.getEmail());
            helper.setSubject("CHEEK! 서비스 메일 인증 안내입니다.");

            String content = getEmailTemplateContent();

            content = content.replace("${verificationCode}", emailVerificationDto.getVerificationCode());

            helper.setText(content, true);

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.CANNOT_READ_TEMPLATE);
        }

        return message;
    }

    private String getEmailTemplateContent() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/email-verification.html");
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }


    private void isEmailDuplicated(String email) {
        EmailVerification emailVerification = emailVerificationRepository.findLatestByEmail(email).orElse(null);
        if (emailVerification != null && emailVerification.isVerified())
            throw new BusinessException(ErrorCode.DUPLICATED_EMAIL);
    }

    @Transactional
    public void verifyCode(EmailVerificationDto emailVerificationDto) {
        EmailVerification emailVerification = emailVerificationRepository.findLatestByEmail(emailVerificationDto.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.EMAIL_NOT_FOUND));

        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isAfter(emailVerification.getValidityPeriod()))
            throw new BusinessException(ErrorCode.EXPIRED_CODE);
        if (!emailVerification.getVerificationCode().equals(emailVerificationDto.getVerificationCode())) {
            log.info("dto code: {}", emailVerificationDto.getVerificationCode());
            log.info("code: {}", emailVerification.getVerificationCode());
            throw new BusinessException(ErrorCode.CODE_NOT_MATCH);
        }

        emailVerification.setVerified();
        log.info("verification code confirmed");
    }

    public boolean validateDomain(String domain) {
        Domain findDomain = domainRepository.findByDomainAndIsValid(domain, true).orElse(null);

        if (findDomain != null) {
            log.info("domain: {}, isValid: {}", findDomain.getDomain(), findDomain.isValid());
            return findDomain.isValid();
        } else {
            log.info("domain: {}, isValid: false", domain);
            return false;
        }
    }

    @Transactional
    public void registerDomain(String domain) {
        Domain findDomain = domainRepository.findByDomainAndIsValid(domain, false).orElse(null);
        if (findDomain != null)
            throw new InProgressException(ErrorCode.IN_PROGRESS);

        domainRepository.save(
                Domain.builder()
                        .domain(domain)
                        .isValid(false)
                        .build()
        );

        log.info("domain registration request successful: {}", domain);
    }
}
