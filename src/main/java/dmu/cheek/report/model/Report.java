package dmu.cheek.report.model;

import dmu.cheek.member.model.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportId;

    private Category category;

    private String title;

    private String content;

    private long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", foreignKey = @ForeignKey(name = "toMember", value = ConstraintMode.NO_CONSTRAINT))
    private Member toMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", foreignKey = @ForeignKey(name = "fromMember", value = ConstraintMode.NO_CONSTRAINT))
    private Member fromMember;

    @Builder
    public Report(Member toMember, Member fromMember, Category category,
                       String title, String content, long categoryId) {
        this.toMember = toMember;
        this.fromMember = fromMember;
        this.category = category;
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }
}
