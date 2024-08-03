package dmu.cheek.upvote.repository;

import dmu.cheek.upvote.model.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpvoteRepository extends JpaRepository<Upvote, Long> {
}
