package dmu.cheek.highlight.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.highlight.model.Highlight;
import dmu.cheek.highlight.model.HighlightDto;
import dmu.cheek.highlight.repository.HighlightRepository;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.MemberDto;
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
import java.util.stream.Collectors;

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

    @Transactional
    public void register(HighlightDto.Request highlightDto) {
        Member member = memberService.findById(highlightDto.getMemberId());
        List<Story> storyList = highlightDto.getStoryIdList().stream()
                .map(storyService::findById)
                .toList();

        Highlight highlight = Highlight.withoutPrimaryKey()
                .thumbnailPicture(
                        highlightDto.getThumbnailPicture() != null ?
                                highlightDto.getThumbnailPicture() : storyList.getFirst().getStoryPicture()
                )
                .storyList(storyList)
                .subject(highlightDto.getSubject())
                .member(member)
                .build();

        highlightRepository.save(highlight);

        log.info("register new highlight: {}, memberId: {}", highlight.getHighlightId(), highlightDto.getMemberId());
    }

    @Transactional
    public void delete(long highlightId) {
        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HIGHLIGHT_NOT_FOUND));

        highlightRepository.delete(highlight);

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
                ).collect(Collectors.toList());
    }

    public List<StoryDto.Response> search(long highlightId, long loginMemberId) {
        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HIGHLIGHT_NOT_FOUND));

        log.info("search highlight: {}", highlightId);

        return highlight.getStoryList().stream()
                .map(story ->
                        StoryDto.Response.builder()
                                .storyId(story.getStoryId())
                                .categoryId(story.getCategory().getCategoryId())
                                .storyPicture(s3Service.getResourceUrl(story.getStoryPicture()))
                                .isUpvoted(story.getUpvoteList().stream()
                                        .anyMatch(u -> u.getMember().getMemberId() == loginMemberId))
                                .upvoteCount(story.getUpvoteList().size())
                                .memberDto(MemberDto.Concise.builder()
                                        .memberId(story.getMember().getMemberId())
                                        .nickname(story.getMember().getNickname())
                                        .profilePicture(s3Service.getResourceUrl(story.getMember().getProfilePicture()))
                                        .build()
                                ).build())
                .sorted(Comparator.comparing(StoryDto.Response::getStoryId).reversed())
                .toList();

    }

    public Highlight findById(long highlightId) {
        return highlightRepository.findById(highlightId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HIGHLIGHT_NOT_FOUND));
    }

    @Transactional
    public void updateHighlightStoryList(long highlightId, HighlightDto.Request highlightDto) {
        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HIGHLIGHT_NOT_FOUND));

        List<Story> storyList = highlightDto.getStoryIdList().stream()
                .map(storyService::findById)
                .toList();

        String thumbnailPicture = highlightDto.getThumbnailPicture() != null ?
                highlightDto.getThumbnailPicture() : storyList.getFirst().getStoryPicture();

        highlight.update(highlightDto.getSubject(), storyList, thumbnailPicture);

        log.info("update highlight: {}", highlightId);
    }
}
