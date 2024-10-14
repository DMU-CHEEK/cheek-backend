package dmu.cheek.highlight.model;

import dmu.cheek.member.model.MemberDto;
import dmu.cheek.story.model.StoryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class HighlightDto {

    private long highlightId;

    private String thumbnailPicture;

    private String subject;

    @Getter @Setter
    public static class Request {
        private List<Long> storyIdList;

        private long memberId;

        private String thumbnailPicture;

        @Size(max = 8, message = "subject must be between 1 and 8 characters")
        @NotBlank(message = "subject cannot be blank")
        private String subject;
    }

    @Builder
    public HighlightDto(long highlightId, String thumbnailPicture, String subject) {
        this.highlightId = highlightId;
        this.thumbnailPicture = thumbnailPicture;
        this.subject = subject;
    }
}
