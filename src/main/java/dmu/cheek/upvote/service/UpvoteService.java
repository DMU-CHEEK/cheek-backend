package dmu.cheek.upvote.service;

import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.noti.model.Notification;
import dmu.cheek.noti.model.Type;
import dmu.cheek.noti.service.NotificationService;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.service.StoryService;
import dmu.cheek.upvote.model.Upvote;
import dmu.cheek.upvote.model.UpvoteDto;
import dmu.cheek.upvote.repository.UpvoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpvoteService {

    private final UpvoteRepository upvoteRepository;
    private final MemberService memberService;
    private final StoryService storyService;
    private final NotificationService notificationService;

    @Transactional
    public void toggleUpvote(UpvoteDto.Request upvoteDto) {
        Member member = memberService.findById(upvoteDto.getMemberId());
        Story story = storyService.findById(upvoteDto.getStoryId());

        Upvote upvote = upvoteRepository.findByStoryAndMember(member, story)
                .orElse(null);

        boolean isNewUpvote = false;

        if (upvote == null) {
            //좋아요 신규 등록
            upvote = Upvote.withoutPrimaryKey()
                    .member(member)
                    .story(story)
                    .isUpvoted(true)
                    .build();

            isNewUpvote = true;

            upvoteRepository.save(upvote);

            log.info("register upvote, storyId: {}", story.getStoryId());
        } else {
            //좋아요 토글링
            boolean currentStatus = upvote.isUpvoted();
            upvote.toggleUpvote(!currentStatus);
            log.info("storyId: {}, upvote status: {}", story.getStoryId(), upvote.isUpvoted());
        }

        if (isNewUpvote || upvote.isUpvoted()) {
            //send notification
            String notiBody = String.format("%s님이 내 스토리에 좋아요를 표시했어요.", upvote.getMember().getNickname());

            notificationService.register(
                    Notification.withoutPrimaryKey()
                            .type(Type.from("UPVOTE"))
                            .typeId(upvote.getUpvoteId())
                            .toMember(upvote.getStory().getMember())
                            .fromMember(upvote.getMember())
                            .body(notiBody)
                            .build()
            );
        }
    }


    public Optional<Upvote> findByMemberId(Long id) {
        return upvoteRepository.findByMemberId(id);
    }

    public List<Member> findTop3MembersWithMostLikesInWeek(
            LocalDateTime startOfWeek, LocalDateTime endOfWeek, Pageable pageable) {
        return upvoteRepository.findTop3MembersWithMostLikesInWeek(startOfWeek, endOfWeek, pageable);
    }
}
