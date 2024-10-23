package dmu.cheek.member.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum MemberType {
    KAKAO, APPLE, NONE;

    @JsonCreator
    public static MemberType from(String memberType) {
        return Stream.of(MemberType.values())
                .filter(m -> m.toString().equals(memberType.toUpperCase()))
                .findFirst()
                .orElse(NONE);
    }
}
