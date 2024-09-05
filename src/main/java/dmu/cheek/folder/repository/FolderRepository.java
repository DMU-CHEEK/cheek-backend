package dmu.cheek.folder.repository;

import dmu.cheek.folder.model.Folder;
import dmu.cheek.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("select f from Folder f where f.folderName = :folderName and f.member = :member")
    Optional<Folder> findByFolderNameAndMember(String folderName, Member member);

    @Query("select f from Folder f where f.member = :member")
    List<Folder> findByMember(Member member);
}
