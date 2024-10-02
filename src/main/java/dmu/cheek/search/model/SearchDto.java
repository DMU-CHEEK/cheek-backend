package dmu.cheek.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder @Getter
@NoArgsConstructor @AllArgsConstructor
public class SearchDto {

    private List<Member> memberDto;
    private List<Story> storyDto;
    private List<Question> questionDto;

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Member {
        private long memberId;
        private String profilePicture;
        private String description;
        private String information;
        private long followerCnt;
        private boolean isFollowing;
        private long resultCnt;
    }

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Story {
        private long storyId;
        private String storyPicture;
        private String text;
        private long resultCnt;
    }

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Question {
        private long questionId;
        private String content;
        private long resultCnt;
    }

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Keyword {
        private List<String> keyword;
    }
}
