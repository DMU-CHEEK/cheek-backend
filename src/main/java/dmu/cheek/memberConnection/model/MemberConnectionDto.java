package dmu.cheek.memberConnection.model;

import lombok.Getter;

@Getter
public class MemberConnectionDto {

    private long memberConnectionId;

    @Getter
    public static class Request {
        private long toMemberId; //요청받은 회원
        private long fromMemberId; //요청한 회원
    }
}
