package dmu.cheek.collection.service;

import dmu.cheek.collection.model.Collection;
import dmu.cheek.collection.model.CollectionDto;
import dmu.cheek.folder.model.Folder;
import dmu.cheek.collection.repository.CollectionRepository;
import dmu.cheek.folder.service.FolderService;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.question.model.Category;
import dmu.cheek.question.service.CategoryService;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.model.StoryDto;
import dmu.cheek.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final StoryService storyService;
    private final FolderService folderService;

    @Transactional
    public void register(CollectionDto.Request collectionDto, MemberInfoDto memberInfoDto) {
        Member member = memberService.findById(memberInfoDto.getMemberId());
        Category category = categoryService.findById(collectionDto.getCategoryId());
        Story story = storyService.findById(collectionDto.getStoryId());

        Folder folder = folderService.findByFolderNameAndMember(collectionDto.getFolderName(), member);

        if (folder == null)
            folder = folderService.register(collectionDto.getFolderName(), member);

        collectionRepository.save(
                Collection.withoutPrimaryKey()
                        .story(story)
                        .member(member)
                        .category(category)
                        .folder(folder)
                        .build()
        );

        folderService.updateThumbnail(folder, story.getStoryPicture());

        log.info("register collection, memberId: {}, folderId: {}, storyId: {}",
                member.getMemberId(), folder.getFolderId(), collectionDto.getStoryId());
    }

    @Transactional
    public void delete(CollectionDto.Delete collectionDto) {
        for (long collectionId : collectionDto.getCollectionIdList()) {
            Collection collection = collectionRepository.findById(collectionId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.COLLECTION_NOT_FOUND));

            collectionRepository.delete(collection);
        }

        log.info("delete collection list");
    }
}
