package dmu.cheek.feed.model;

import dmu.cheek.member.model.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder @Getter
@NoArgsConstructor @AllArgsConstructor
public class FeedDto {

    private String type; //QUESTION|STORY
    private Story storyDto;
    private Question questionDto;
    private MemberDto.Concise memberDto;
    private LocalDateTime modifiedAt;

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Story {
        private long storyId;
        private String storyPicture;
        private boolean isUpvoted;
        private int upvoteCount;
    }

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Question {
        private long questionId;
        private String content;
        private int storyCnt;
    }

}
