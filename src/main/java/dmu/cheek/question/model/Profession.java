package dmu.cheek.question.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Profession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profession_id")
    private long professionId;

    private String profession;

    @OneToMany(mappedBy = "profession")
    private List<Question> questionList = new ArrayList<>();
}
