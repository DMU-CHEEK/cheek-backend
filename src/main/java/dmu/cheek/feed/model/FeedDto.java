package dmu.cheek.feed.model;

import dmu.cheek.member.model.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder @Getter
@NoArgsConstructor @AllArgsConstructor
public class FeedDto {

    private List<Story> storyDto;
    private List<Question> questionDto;

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Story {
        private long storyId;
        private String storyPicture;
        private boolean isUpvoted;
        private int upvoteCount;
        private LocalDateTime modifiedAt;
        private MemberDto.Concise memberDto;
    }

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Question {
        private long questionId;
        private String content;
        private MemberDto.Concise memberDto;
        private LocalDateTime modifiedAt;
    }

}
