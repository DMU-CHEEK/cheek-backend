package dmu.cheek.emailVerification.service;

import dmu.cheek.emailVerification.converter.EmailVerificationConverter;
import dmu.cheek.emailVerification.model.EmailVerification;
import dmu.cheek.emailVerification.model.EmailVerificationDto;
import dmu.cheek.emailVerification.repository.EmailVerificationRepository;
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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailVerificationConverter emailVerificationConverter;
    private final JavaMailSender javaMailSender;
    private final ResourceLoader resourceLoader;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Transactional
    public void registerEmailVerification(EmailVerificationDto emailVerificationDto) throws MessagingException {

        String verificationCode = createVerificationCode();
        emailVerificationDto.setup(verificationCode);

        sendEmailVerification(emailVerificationDto);

        EmailVerification emailVerification = emailVerificationConverter.convertToEntity(emailVerificationDto);
        emailVerificationRepository.save(emailVerification);
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
        if (isEmailDuplicated(emailVerificationDto.getEmail())) {
            throw new RuntimeException("duplicated email"); //TODO: exception
        }

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
            throw new RuntimeException("Error while reading email template: " + e.getMessage());
        }

        return message;
    }

    private String getEmailTemplateContent() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/email-verification.html");
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }


    private boolean isEmailDuplicated(String email) {
        return emailVerificationRepository.findByEmail(email).isEmpty() ? false : true;
    }
}
