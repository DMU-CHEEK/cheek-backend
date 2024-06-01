package dmu.cheek.emailVerification.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String validityPeriod;

    @Builder
    public EmailVerification(String email, String verificationCode, String validityPeriod) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.validityPeriod = validityPeriod;
    }
}
