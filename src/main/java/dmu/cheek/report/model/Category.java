package dmu.cheek.report.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum Category {

    MEMBER, QUESTION, STORY, NONE;

    @JsonCreator
    public static Category from(String category) {
        return Stream.of(Category.values())
                .filter(c -> c.toString().equals(category.toUpperCase()))
                .findFirst()
                .orElse(NONE);
    }
}
