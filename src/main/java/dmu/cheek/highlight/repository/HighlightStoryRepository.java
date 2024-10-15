package dmu.cheek.highlight.repository;

import dmu.cheek.highlight.model.HighlightStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HighlightStoryRepository extends JpaRepository<HighlightStory, Long> {

    @Query("select h from HighlightStory h where h.highlight.highlightId = :highlightId")
    List<HighlightStory> findByHighlightId(long highlightId);
}
