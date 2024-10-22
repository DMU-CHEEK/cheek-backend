package dmu.cheek.noti.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum Type {

    ROLE, MEMBER_CONNECTION, STORY, UPVOTE, NONE;

    @JsonCreator
    public static Type from(String type) {
        return Stream.of(Type.values())
                .filter(t -> t.toString().equals(type.toUpperCase()))
                .findFirst()
                .orElse(NONE);
    }
}
