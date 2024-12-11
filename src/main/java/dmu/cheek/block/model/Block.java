package dmu.cheek.block.model;

import dmu.cheek.member.model.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long blockId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "to_member_id", foreignKey = @ForeignKey(name = "toMember", value = ConstraintMode.NO_CONSTRAINT))
    private Member toMember;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "from_member_id", foreignKey = @ForeignKey(name = "fromMember", value = ConstraintMode.NO_CONSTRAINT))
    private Member fromMember;

    @Builder
    public Block(Member toMember, Member fromMember) {
        this.toMember = toMember;
        this.fromMember = fromMember;
    }
}
