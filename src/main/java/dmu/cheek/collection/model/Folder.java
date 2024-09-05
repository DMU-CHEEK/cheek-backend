package dmu.cheek.collection.model;

import dmu.cheek.member.model.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private long folderId;

    @Column(name = "folder_name")
    private String folderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
    private List<Collection> collectionList;

    @Builder(builderMethodName = "withoutPrimaryKey")
    public Folder(String folderName, Member member) {
        this.folderName = folderName;
        this.member = member;
    }
}