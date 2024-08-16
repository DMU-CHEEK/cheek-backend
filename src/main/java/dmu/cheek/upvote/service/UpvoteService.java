package dmu.cheek.upvote.service;

import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.service.StoryService;
import dmu.cheek.upvote.model.Upvote;
import dmu.cheek.upvote.model.UpvoteDto;
import dmu.cheek.upvote.repository.UpvoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpvoteService {

    private final UpvoteRepository upvoteRepository;
    private final MemberService memberService;
    private final StoryService storyService;

    @Transactional
    public void toggleUpvote(UpvoteDto.Request upvoteDto) {
        Member member = memberService.findById(upvoteDto.getMemberId());
        Story story = storyService.findById(upvoteDto.getStoryId());

        Upvote findUpvote = upvoteRepository.findByStoryAndMember(member, story)
                .orElse(null);

        if (findUpvote == null) {
            upvoteRepository.save(
                    Upvote.builder().
                            member(member)
                            .story(story)
                            .isUpvoted(true)
                            .build()
            );
            log.info("register upvote, storyId: {}", story.getStoryId());
        }

        else {
            if (!findUpvote.isUpvoted())
                findUpvote.toggleUpvote(true);

            else
                findUpvote.toggleUpvote(false);

            log.info("storyId: {}, upvote status: {}", story.getStoryId(), findUpvote.isUpvoted());
        }
    }

    public Optional<Upvote> findByUpvoteMemberId(Long id){

        return upvoteRepository.findByMemberId(id);
    }
}
