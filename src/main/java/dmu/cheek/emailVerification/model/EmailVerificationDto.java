package dmu.cheek.emailVerification.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor
public class EmailVerificationDto {

    private long emailVerificationId;

    @Email
    @NotBlank(message = "email cannot be blank")
    private String email;

    private String verificationCode;

    private LocalDateTime validityPeriod;

    private boolean isVerified;

    @Builder(builderMethodName = "allFields")
    public EmailVerificationDto(long emailVerificationId, String email, String verificationCode, LocalDateTime validityPeriod, boolean isVerified) {
        this.emailVerificationId = emailVerificationId;
        this.email = email;
        this.verificationCode = verificationCode;
        this.validityPeriod = validityPeriod;
        this.isVerified = isVerified;
    }

    @Builder(builderMethodName = "withoutPrimaryKey")
    public EmailVerificationDto(String email, String verificationCode, LocalDateTime validityPeriod, boolean isVerified) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.validityPeriod = validityPeriod;
        this.isVerified = isVerified;
    }

    public void setup(String verificationCode) {
        LocalDateTime currentTime = LocalDateTime.now();
        long expirationMillis = 180000; //3분
        LocalDateTime expirationTime = currentTime.plus(expirationMillis, ChronoUnit.MILLIS); //현재시간 + 3분

        this.validityPeriod = expirationTime;
        this.verificationCode = verificationCode;
        this.isVerified = false;
    }

}
