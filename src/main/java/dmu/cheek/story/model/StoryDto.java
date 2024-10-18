package dmu.cheek.story.model;

import dmu.cheek.member.model.MemberDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StoryDto {

    private long storyId;

    private String storyPicture;

    private String text;

    @Getter
    public static class Request {
        private long categoryId;
        private long memberId;
        private long questionId;
        private String text;
    }

    @Getter @Builder
    public static class ResponseList {
        private long storyId;
        private long categoryId;
        private String storyPicture;
        private boolean isUpvoted;
        private int upvoteCount;
        private LocalDateTime modifiedAt;
    }

    @Getter @Builder
    public static class ResponseOne {
        private long storyId;
        private long categoryId;
        private String storyPicture;
        private boolean isUpvoted;
        private int upvoteCount;
        private MemberDto.Concise memberDto;
    }
}
