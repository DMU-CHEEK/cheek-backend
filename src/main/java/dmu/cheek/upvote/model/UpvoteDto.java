package dmu.cheek.upvote.model;

import lombok.Getter;

public class UpvoteDto {

    private long upvoteId;

    private boolean isUpvoted;

    @Getter
    public static class Request {

        private long memberId;
        private long storyId;
    }
}
