package dmu.cheek.collection.service;

import dmu.cheek.collection.model.Collection;
import dmu.cheek.collection.model.CollectionDto;
import dmu.cheek.collection.repository.CollectionRepository;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.question.model.Category;
import dmu.cheek.question.service.CategoryService;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final StoryService storyService;

    @Transactional
    public void register(CollectionDto.Request collectionDto) {
        Member member = memberService.findById(collectionDto.getMemberId());
        Category category = categoryService.findById(collectionDto.getCategoryId());
        Story story = storyService.findById(collectionDto.getStoryId());

        collectionRepository.save(
                Collection.withoutPrimaryKey()
                        .story(story)
                        .member(member)
                        .category(category)
                        .thumbnailPicture(story.getStoryPicture())
                        .build()
        );

        log.info("register collection, memberId: {}, categoryId: {}, storyId: {}",
                collectionDto.getMemberId(), collectionDto.getCategoryId(), collectionDto.getStoryId());
    }

    @Transactional
    public void delete(long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COLLECTION_NOT_FOUND));

        collectionRepository.delete(collection);

        log.info("delete collection, collectionId: {}", collectionId);
    }
}
