package dmu.cheek.search.model;

import dmu.cheek.member.model.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder @Getter
@NoArgsConstructor @AllArgsConstructor
public class SearchDto {

    private List<Member> memberDto;
    private List<Story> storyDto;
    private List<Question> questionDto;
    private long memberResCnt; //TODO
    private long storyResCnt; //TODO
    private long questionResCnt; //TODO


    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Member {
        private long memberId;
        private String nickname;
        private String profilePicture;
        private String description;
        private String information;
        private long followerCnt;
        private boolean isFollowing;
    }

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Story {
        private long storyId;
        private String storyPicture;
        private String text;
        private LocalDateTime modifiedAt;
    }

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Question {
        private long questionId;
        private String content;
        private LocalDateTime modifiedAt;
        private long categoryId;
        private MemberDto.Concise memberDto;
    }

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Keyword {
        private List<String> keyword;
    }
}
