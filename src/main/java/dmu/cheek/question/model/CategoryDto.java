package dmu.cheek.question.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDto {

    private long categoryId;

    private String name;
}
