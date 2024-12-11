package dmu.cheek.block.repository;

import dmu.cheek.block.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long> {
}
