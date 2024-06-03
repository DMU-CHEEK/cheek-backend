package dmu.cheek.emailVerification.converter;

import dmu.cheek.emailVerification.model.EmailVerification;
import dmu.cheek.emailVerification.model.EmailVerificationDto;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationConverter {

    public EmailVerificationDto convertToDto(EmailVerification emailVerification) {
        if (emailVerification == null)
            return null;

        return EmailVerificationDto.allFields()
                .emailVerificationId(emailVerification.getEmailVerificationId())
                .email(emailVerification.getEmail())
                .verificationCode(emailVerification.getVerificationCode())
                .validityPeriod(emailVerification.getValidityPeriod())
                .build();
    }

    public EmailVerification convertToEntity(EmailVerificationDto emailVerificationDto) {
        if (emailVerificationDto == null)
            return null;

        return EmailVerification.allFields()
                .emailVerificationId(emailVerificationDto.getEmailVerificationId())
                .email(emailVerificationDto.getEmail())
                .verificationCode(emailVerificationDto.getVerificationCode())
                .validityPeriod(emailVerificationDto.getValidityPeriod())
                .build();
    }
}
