package dmu.cheek.question.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private long categoryId;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Question> questionList = new ArrayList<>();
}
