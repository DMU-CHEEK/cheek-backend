package dmu.cheek.upvote.repository;

import dmu.cheek.member.model.Member;
import dmu.cheek.story.model.Story;
import dmu.cheek.upvote.model.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UpvoteRepository extends JpaRepository<Upvote, Long> {

    @Query("select u from Upvote u where u.member = :member and u.story = :story")
    Optional<Upvote> findByStoryAndMember(Member member, Story story);
}
