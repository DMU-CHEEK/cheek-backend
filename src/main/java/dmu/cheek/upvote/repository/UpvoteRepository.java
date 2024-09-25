package dmu.cheek.upvote.repository;

import dmu.cheek.member.model.Member;
import dmu.cheek.story.model.Story;
import dmu.cheek.upvote.model.Upvote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UpvoteRepository extends JpaRepository<Upvote, Long> {

    @Query("select u from Upvote u where u.member = :member and u.story = :story")
    Optional<Upvote> findByStoryAndMember(Member member, Story story);

    @Query("select u from Upvote u where u.member.memberId = :memberId")
    Optional<Upvote> findByMemberId(Long memberId);

    @Query("SELECT u.member FROM Upvote u " +
            "WHERE u.isUpvoted = true AND u.modifiedAt BETWEEN :startDate AND :endDate " +
            "GROUP BY u.member ORDER BY COUNT(u) DESC")
    List<Member> findTop3MembersWithMostLikesInWeek(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate,
                                                    Pageable pageable);

}
