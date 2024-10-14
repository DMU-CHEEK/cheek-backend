package dmu.cheek.highlight.model;

import dmu.cheek.member.model.MemberDto;
import dmu.cheek.story.model.StoryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class HighlightDto {

    private long highlightId;

    private String thumbnailPicture;

    private String subject;

    @Getter
    public static class Request {
        private List<Long> storyIdList;

        private long memberId;

        @Size(max = 8, message = "subject must be between 1 and 8 characters")
        @NotBlank(message = "subject cannot be blank")
        private String subject;
    }

    @Getter @Builder
    public static class ResponseOne {
        private List<StoryDto> storyList;
        private long categoryId;
        private boolean isUpvoted;
        private int upvoteCount;
        private MemberDto.Concise memberDto;
    }

    @Getter @Builder
    public static class ResponseList {
        private long highlightId;
        private long categoryId;
        private String thumbnailPicture;
    }

    @Builder
    public HighlightDto(long highlightId, String thumbnailPicture, String subject) {
        this.highlightId = highlightId;
        this.thumbnailPicture = thumbnailPicture;
        this.subject = subject;
    }
}
