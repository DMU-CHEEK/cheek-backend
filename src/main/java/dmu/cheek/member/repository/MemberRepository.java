package dmu.cheek.member.repository;

import dmu.cheek.member.constant.MemberType;
import dmu.cheek.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.email = :email")
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m where m.nickname = :nickname")
    Optional<Member> findByNickname(String nickname);

    List<Member> findByNicknameContaining(String keyword);

    @Query("select m from Member m where m.refreshToken = :refreshToken")
    Optional<Member> findByRefreshToken(String refreshToken);

    @Query("select m from Member m where m.email = :email and m.memberType = :memberType")
    Optional<Member> findByEmailAndMemberType(String email, MemberType memberType);
}
