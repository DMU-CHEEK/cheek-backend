package dmu.cheek.emailVerification.repository;

import dmu.cheek.emailVerification.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    @Query("select e from EmailVerification e where e.email = :email")
    Optional<EmailVerification> findByEmail(String email);
}
