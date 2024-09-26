package dmu.cheek.story.model;

import lombok.Builder;
import lombok.Getter;

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
    public static class Response {
        private long storyId;
        private long categoryId;
        private String storyPicture;
        private boolean isUpvoted;
        private int upvoteCount;
    }
}
