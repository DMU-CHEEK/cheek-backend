package dmu.cheek.memberConnection.repository;

import dmu.cheek.memberConnection.model.MemberConnection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberConnectionRepository extends JpaRepository<MemberConnection, Long> {
}
