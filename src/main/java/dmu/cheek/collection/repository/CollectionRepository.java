package dmu.cheek.collection.repository;

import dmu.cheek.collection.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

}
