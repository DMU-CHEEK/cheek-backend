package dmu.cheek.memberConnection.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberConnectionDto {

    private long memberConnectionId;

    @Getter
    public static class Request {
        private long toMemberId; //요청받은 회원
        private long fromMemberId; //요청한 회원
    }

    @Getter @Builder
    public static class Response {
        private long memberId;
        private String profilePicture;
        private String nickname;
        private String information;
        private int followerCnt;
        private boolean isFollowing;
    }
}
