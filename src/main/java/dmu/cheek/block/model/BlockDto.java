package dmu.cheek.block.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class BlockDto {

    private long blockId;

    @Builder @NoArgsConstructor @AllArgsConstructor @Getter
    public static class Response {
        private long blockId;
        private MemberDto memberDto;
    }

    @Builder @NoArgsConstructor @AllArgsConstructor @Getter
    public static class MemberDto {
        private long memberId;
        private String nickname;
        private String information;
        private String profilePicture;
    }
}
