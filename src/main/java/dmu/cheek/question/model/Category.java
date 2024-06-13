package dmu.cheek.question.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private long categoryId;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Question> questionList = new ArrayList<>();

    @Builder(builderMethodName = "allFields")
    public Category(long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    @Builder(builderMethodName = "withoutPrimaryKey")
    public Category(String name) {
        this.name = name;
    }
}
