package dmu.cheek.collection.service;

import dmu.cheek.collection.model.Folder;
import dmu.cheek.collection.repository.FolderRepository;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderService {

    private final FolderRepository folderRepository;

    public Folder findById(long folderId) {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLDER_NOT_FOUND));
    }

    public Folder findByFolderNameAndMember(String folderName, Member member) {
        return folderRepository.findByFolderNameAndMember(folderName, member)
                .orElse(null);
    }

    @Transactional
    public Folder register(String folderName, Member member) {

        Folder folder = Folder.withoutPrimaryKey()
                .folderName(folderName)
                .member(member)
                .build();

        folderRepository.save(folder);

        log.info("register folder, memberId: {}, folderId: {}", member.getMemberId(), folder.getFolderId());

        return folder;
    }

    @Transactional
    public void delete(Folder folder) {
        folderRepository.delete(folder);

        log.info("delete folder, memberId: {}", folder.getMember().getMemberId(), folder.getFolderId());
    }
}
