package dmu.cheek.member.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum Role {
    MENTOR, MENTEE, ADMIN, NONE;

    @JsonCreator
    public static Role from(String role) {
        return Stream.of(Role.values())
                .filter(r -> r.toString().equals(role.toUpperCase()))
                .findFirst()
                .orElse(NONE);
    }
}
