package dmu.cheek.highlight.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.highlight.model.Highlight;
import dmu.cheek.highlight.model.HighlightDto;
import dmu.cheek.highlight.repository.HighlightRepository;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.s3.model.S3Dto;
import dmu.cheek.s3.service.S3Service;
import dmu.cheek.story.converter.StoryConverter;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.model.StoryDto;
import dmu.cheek.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HighlightService {

    private final MemberService memberService;
    private final StoryService storyService;
    private final S3Service s3Service;
    private final HighlightRepository highlightRepository;
    private final StoryConverter storyConverter;

    @Transactional
    public void register(MultipartFile thumbnailPicture, HighlightDto.Request highlightDto) {
        Member member = memberService.findById(highlightDto.getMemberId());
        List<Story> storyList = highlightDto.getStoryIdList().stream()
                .map(storyService::findById)
                .collect(Collectors.toList());

        S3Dto s3Dto = s3Service.saveFile(thumbnailPicture);

        Highlight highlight = Highlight.withoutPrimaryKey()
                .thumbnailPicture(s3Dto.getStoreFileName())
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

        return highlightRepository.findByMember(member)
                .stream().map(
                        h -> HighlightDto.builder()
                                .highlightId(h.getHighlightId())
                                .thumbnailPicture(s3Service.getResourceUrl(h.getThumbnailPicture()))
                                .subject(h.getSubject())
                                .build()
                ).collect(Collectors.toList());
    }

    public HighlightDto.Response search(long highlightId) {
        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HIGHLIGHT_NOT_FOUND));

        List<StoryDto> storyList = highlight.getStoryList().stream().map(
                story -> StoryDto.builder()
                        .storyId(story.getStoryId())
                        .storyPicture(s3Service.getResourceUrl(story.getStoryPicture()))
                        .build()
        ).toList();

        log.info("search highlight: ", highlightId);

        return HighlightDto.Response.builder()
                .storyList(storyList)
                .build();
    }
}
