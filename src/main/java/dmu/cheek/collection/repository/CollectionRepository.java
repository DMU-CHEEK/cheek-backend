package dmu.cheek.collection.repository;

import dmu.cheek.collection.model.Collection;
import dmu.cheek.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query("select c from Collection c where c.member = :member")
    List<Collection> findByMember(Member member);
}
