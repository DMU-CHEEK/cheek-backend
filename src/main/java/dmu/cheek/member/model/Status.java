package dmu.cheek.member.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum Status {

    PENDING, COMPLETE, NONE;

    @JsonCreator
    public static Status from(String status) {
        return Stream.of(Status.values())
                .filter(s -> s.toString().equals(status.toUpperCase()))
                .findFirst()
                .orElse(NONE);
    }
}
