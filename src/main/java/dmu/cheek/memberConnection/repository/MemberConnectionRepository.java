package dmu.cheek.memberConnection.repository;

import dmu.cheek.memberConnection.model.MemberConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberConnectionRepository extends JpaRepository<MemberConnection, Long> {

    @Query("select m from MemberConnection m where m.toMember.memberId = :toMemberId and m.fromMember.memberId = :fromMemberId")
    Optional<MemberConnection> findByToMemberAndfromMember(long toMemberId, long fromMemberId);
}
