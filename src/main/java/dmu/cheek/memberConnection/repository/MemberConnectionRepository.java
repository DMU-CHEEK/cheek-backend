package dmu.cheek.memberConnection.repository;

import dmu.cheek.memberConnection.model.MemberConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberConnectionRepository extends JpaRepository<MemberConnection, Long> {

    @Query("select m from MemberConnection m where m.toMember.memberId = :toMemberId and m.fromMember.memberId = :fromMemberId")
    Optional<MemberConnection> findByToMemberAndFromMember(long toMemberId, long fromMemberId);

    @Query("select m from MemberConnection m where m.toMember.memberId = :memberId")
    List<MemberConnection> findByToMember(long memberId);

    @Query("select m from MemberConnection m where m.fromMember.memberId = :memberId")
    List<MemberConnection> findByFromMember(long memberId);
}
