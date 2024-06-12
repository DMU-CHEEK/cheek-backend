package dmu.cheek.question.converter;

import dmu.cheek.question.model.Category;
import dmu.cheek.question.model.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public Category convertToEntity(CategoryDto categoryDto) {
        if (categoryDto == null)
            return null;

        return Category.allFields()
                .categoryId(categoryDto.getCategoryId())
                .name(categoryDto.getName())
                .build();
    }

    public CategoryDto convertToDto(Category category) {
        if (category == null)
            return null;

        return CategoryDto.allFields()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .build();
    }
}
