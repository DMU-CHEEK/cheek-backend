package dmu.cheek.highlight.repository;

import dmu.cheek.highlight.model.Highlight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HighlightRepository extends JpaRepository<Highlight, Long> {

}
