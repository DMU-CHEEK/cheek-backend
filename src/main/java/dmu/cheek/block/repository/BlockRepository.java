package dmu.cheek.block.repository;

import dmu.cheek.block.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("select b from Block b where b.fromMember.memberId = :memberId")
    List<Block> findByFromMemberId(long memberId);
}
