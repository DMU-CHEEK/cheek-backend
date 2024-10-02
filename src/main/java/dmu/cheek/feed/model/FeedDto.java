package dmu.cheek.feed.model;

import dmu.cheek.member.model.MemberDto;
import dmu.cheek.question.model.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private MemberDto.Concise memberDto;
    }

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Question {
        private long questionId;
        private String content;
        private MemberDto.Concise memberDto;
    }

}
