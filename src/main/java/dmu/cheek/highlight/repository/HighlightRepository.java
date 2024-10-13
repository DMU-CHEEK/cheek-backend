package dmu.cheek.highlight.repository;

import dmu.cheek.highlight.model.Highlight;
import dmu.cheek.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HighlightRepository extends JpaRepository<Highlight, Long> {

    @Query("select h from Highlight h where h.member = :member order by h.highlightId desc")
    List<Highlight> findByMemberOrderByIdDesc(Member member);

}
