package dmu.cheek.question.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryDto {

    private long categoryId;

    private String name;

    @Builder(builderMethodName = "withoutPrimaryKey")
    public CategoryDto(String name) {
        this.name = name;
    }

    @Builder(builderMethodName = "allFields")
    public CategoryDto(long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
