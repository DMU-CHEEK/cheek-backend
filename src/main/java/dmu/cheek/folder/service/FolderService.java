package dmu.cheek.folder.service;

import dmu.cheek.folder.model.Folder;
import dmu.cheek.folder.model.FolderDto;
import dmu.cheek.folder.repository.FolderRepository;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.s3.service.S3Service;
import dmu.cheek.story.model.StoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderService {

    private final MemberService memberService;
    private final FolderRepository folderRepository;
    private final S3Service s3Service;

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
    public void delete(long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLDER_NOT_FOUND));

        folderRepository.delete(folder);

        log.info("delete folder, memberId: {}", folder.getMember().getMemberId(), folder.getFolderId());
    }

    public List<FolderDto.Response> findDtoByMember(MemberInfoDto memberInfoDto) {
        Member member = memberService.findById(memberInfoDto.getMemberId());
        List<Folder> folderList = folderRepository.findByMember(member);

        log.info("search folder list, memberId: {}", memberInfoDto.getMemberId());

        return folderList.stream()
                .map(folder -> FolderDto.Response.builder()
                        .folderId(folder.getFolderId())
                        .folderName(folder.getFolderName())
                        .thumbnailPicture(s3Service.getResourceUrl(folder.getThumbnailPicture()))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateThumbnail(Folder folder, String thumbnailPicture) {
        folder.updateThumbnailPicture(thumbnailPicture);
    }

    public List<StoryDto.Collection> search(long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLDER_NOT_FOUND));

        log.info("retrieve scraped story list, folderId: {}", folderId);

        return folder.getCollectionList().stream()
                .map(collection -> StoryDto.Collection.builder()
                        .collectionId(collection.getCollectionId())
                        .storyId(collection.getStory().getStoryId())
                        .modifiedAt(collection.getModifiedAt())
                        .storyPicture(s3Service.getResourceUrl(collection.getStory().getStoryPicture()))
                        .build()
                )
                .toList();
    }
}
