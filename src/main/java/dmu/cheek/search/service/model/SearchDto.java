package dmu.cheek.search.service.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public class SearchDto {

    private List<Member> memberDto;
    private List<Story> storyDto;
    private List<Question> questionDto;

    @Builder @Getter
    public static class Member {
        private long memberId;
        private String profilePicture;
        private String description;
        private String information;
        private long followerCnt;
        private boolean isFollowing;
        private long resultCnt;
    }

    @Builder
    public static class Story {
        private long storyId;
        private String storyPicture;
        private String text;
        private long resultCnt;
    }

    @Builder
    public static class Question {
        private long questionId;
        private String content;
        private long resultCnt;
    }
}
