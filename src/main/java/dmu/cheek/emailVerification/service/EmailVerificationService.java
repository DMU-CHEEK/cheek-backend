package dmu.cheek.emailVerification.service;

import dmu.cheek.emailVerification.converter.EmailVerificationConverter;
import dmu.cheek.emailVerification.model.EmailVerification;
import dmu.cheek.emailVerification.model.EmailVerificationDto;
import dmu.cheek.emailVerification.repository.DomainRepository;
import dmu.cheek.emailVerification.repository.EmailVerificationRepository;
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
import java.io.UnsupportedEncodingException;
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

        log.info("save emailVerification: ", emailVerification.getEmail());
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
            throw new RuntimeException("could not read template" + e.getMessage());
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
        if (emailVerification != null && emailVerification.isVerified() == true)
            throw new RuntimeException("duplicated email"); //TODO: exception
    }

    public void verifyCode(EmailVerificationDto emailVerificationDto) {
        EmailVerification emailVerification = emailVerificationRepository.findLatestByEmail(emailVerificationDto.getEmail())
                .orElseThrow(RuntimeException::new); //TODO: exception

        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isAfter(emailVerification.getValidityPeriod()))
            throw new RuntimeException("validity period has expired"); //TODO: exception
        if (!emailVerification.getVerificationCode().equals(emailVerificationDto.getVerificationCode()))
            throw new RuntimeException("verification code does not match"); //TODO: exception

        emailVerification.setVerified();
        log.info("verification code confirmed");
    }

    public boolean validateDomain(String domain) {
        Domain findDomain = domainRepository.findByDomainAndIsValid(domain, true)
                .orElse(null);

        return findDomain != null;
    }

    @Transactional
    public void registerDomain(String domain) {
        boolean domainValid = validateDomain(domain);
        if (domainValid)
            throw new RuntimeException("already exist domain"); //TODO: exception

        domainRepository.save(
                Domain.builder()
                        .domain(domain)
                        .isValid(false)
                        .build()
        );

        log.info("domain registration request successful: {}", domain);
    }
}
