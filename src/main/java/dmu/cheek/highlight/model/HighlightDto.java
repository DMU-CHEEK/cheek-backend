package dmu.cheek.highlight.model;

import dmu.cheek.story.model.StoryDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class HighlightDto {

    private long highlightId;

    private String thumbnailPicture;

    @Getter
    public static class Request {
        private List<Long> storyIdList;
        private long memberId;
    }

    @Getter @Builder
    public static class Response {
        private List<StoryDto> storyList;
    }

    @Builder
    public HighlightDto(long highlightId, String thumbnailPicture) {
        this.highlightId = highlightId;
        this.thumbnailPicture = thumbnailPicture;
    }
}
