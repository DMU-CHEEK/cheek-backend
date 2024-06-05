package dmu.cheek.emailVerification.repository;

import dmu.cheek.member.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    @Query("select d from Domain d where d.domain = :domain")
    Optional<Domain> findByDomain(String domain);
}
