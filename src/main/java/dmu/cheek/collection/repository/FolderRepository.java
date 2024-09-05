package dmu.cheek.collection.repository;

import dmu.cheek.collection.model.Folder;
import dmu.cheek.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("select f from Folder f where f.folderName = :folderName and f.member = :member")
    Optional<Folder> findByFolderNameAndMember(String folderName, Member member);

}
