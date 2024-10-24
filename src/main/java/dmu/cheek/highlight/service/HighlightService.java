package dmu.cheek.highlight.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.highlight.model.Highlight;
import dmu.cheek.highlight.model.HighlightDto;
import dmu.cheek.highlight.model.HighlightStory;
import dmu.cheek.highlight.repository.HighlightRepository;
import dmu.cheek.highlight.repository.HighlightStoryRepository;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.s3.service.S3Service;
import dmu.cheek.story.converter.StoryConverter;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.model.StoryDto;
import dmu.cheek.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HighlightService {

    private final MemberService memberService;
    private final StoryService storyService;
    private final HighlightRepository highlightRepository;
    private final StoryConverter storyConverter;
    private final S3Service s3Service;
    private final HighlightStoryRepository highlightStoryRepository;

    @Transactional
    public void register(HighlightDto.Request highlightDto, MemberInfoDto memberInfoDto) {
        Member member = memberService.findById(memberInfoDto.getMemberId());
        List<Story> storyList = highlightDto.getStoryIdList().stream()
                .map(storyService::findById)
                .toList();

        Highlight highlight = Highlight.withoutPrimaryKey()
                .thumbnailPicture(
                        highlightDto.getThumbnailPicture() != null ?
                                highlightDto.getThumbnailPicture() : storyList.getFirst().getStoryPicture()
                )
                .subject(highlightDto.getSubject())
                .member(member)
                .build();

        highlightRepository.save(highlight);

        List<HighlightStory> highlightStoryList = storyList.stream()
                .map(story -> HighlightStory.withoutPrimaryKey()
                        .story(story)
                        .highlight(highlight)
                        .build()
                ).toList();

        highlightStoryRepository.saveAll(highlightStoryList);

        log.info("register new highlight: {}, memberId: {}", highlight.getHighlightId(), memberInfoDto.getMemberId());
    }

    @Transactional
    public void delete(long highlightId) {
        List<HighlightStory> highlightStoryList = highlightStoryRepository.findByHighlightId(highlightId);

        highlightStoryRepository.deleteAll(highlightStoryList);
        highlightRepository.delete(highlightStoryList.getFirst().getHighlight());

        log.info("delete highlight: {}", highlightId);
    }

    public List<HighlightDto> searchByMember(long memberId) {
        Member member = memberService.findById(memberId);

        log.info("search highlight list by member: {}", memberId);

        return highlightRepository.findByMemberOrderByIdDesc(member)
                .stream().map(
                        h -> HighlightDto.builder()
                                .highlightId(h.getHighlightId())
                                .thumbnailPicture(s3Service.getResourceUrl(h.getThumbnailPicture()))
                                .subject(h.getSubject())
                                .build()
                ).toList();
    }

    public HighlightDto.Response search(long highlightId) {
        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HIGHLIGHT_NOT_FOUND));

        log.info("search highlight: {}", highlightId);

        List<Long> storyIds = highlight.getHighlightStoryList().stream()
                .map(highlightStory -> highlightStory.getStory().getStoryId())
                .sorted(Comparator.reverseOrder())
                .toList();

        return HighlightDto.Response.builder()
                .highlightId(highlightId)
                .storyId(storyIds)
                .build();
    }

    public Highlight findById(long highlightId) {
        return highlightRepository.findById(highlightId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HIGHLIGHT_NOT_FOUND));
    }

    @Transactional
    public void updateHighlightStoryList(long highlightId, HighlightDto.Request highlightDto) {
        List<HighlightStory> highlightStoryList = highlightStoryRepository.findByHighlightId(highlightId);
        highlightStoryRepository.deleteAll(highlightStoryList);

        List<Story> storyList = highlightDto.getStoryIdList().stream()
                .map(storyService::findById)
                .toList();

        String thumbnailPicture = highlightDto.getThumbnailPicture() != null ?
                highlightDto.getThumbnailPicture() : storyList.getFirst().getStoryPicture();

        Highlight highlight = highlightStoryList.getFirst().getHighlight();
        highlight.update(highlightDto.getSubject(), thumbnailPicture);

        highlightStoryRepository.saveAll(
                storyList.stream()
                        .map(story -> HighlightStory.withoutPrimaryKey()
                                .story(story)
                                .highlight(highlight)
                                .build()
                        ).toList()
        );

        log.info("update highlight: {}", highlightId);
    }
}
