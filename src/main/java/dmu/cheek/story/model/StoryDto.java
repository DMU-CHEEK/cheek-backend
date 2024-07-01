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
}
