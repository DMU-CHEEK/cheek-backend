package dmu.cheek.story.repository;

import dmu.cheek.story.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
