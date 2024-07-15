package dmu.cheek.highlight.model;

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

    @Builder
    public HighlightDto(long highlightId, String thumbnailPicture) {
        this.highlightId = highlightId;
        this.thumbnailPicture = thumbnailPicture;
    }
}
