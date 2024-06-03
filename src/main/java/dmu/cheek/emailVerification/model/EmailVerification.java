package dmu.cheek.emailVerification.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_verification_id")
    private long emailVerificationId;

    private String email;

    @Column(name = "verifation_code")
    private String verificationCode;

    @Column(name = "validity_period")
    private LocalDateTime validityPeriod;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Builder(builderMethodName = "withoutPrimaryKey")
    public EmailVerification(String email, String verificationCode, LocalDateTime validityPeriod, boolean isVerified) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.validityPeriod = validityPeriod;
        this.isVerified = isVerified;
    }

    @Builder(builderMethodName = "allFields")
    public EmailVerification(long emailVerificationId, String email, String verificationCode, LocalDateTime validityPeriod, boolean isVerified) {
        this.emailVerificationId = emailVerificationId;
        this.email = email;
        this.verificationCode = verificationCode;
        this.validityPeriod = validityPeriod;
        this.isVerified = isVerified;
    }

    public void setVerified() {
        this.isVerified = true;
    }
}
