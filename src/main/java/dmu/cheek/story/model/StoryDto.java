package dmu.cheek.story.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoryDto {

    private long storyId;

    private String storyPicture;

    @Getter
    public static class Request {
        private long categoryId;
        private long memberId;
        private long questionId;
    }

    @Getter @Builder
    public static class Response {
        private long storyId;
        private long categoryId;
        private String storyPicture;
    }
}
