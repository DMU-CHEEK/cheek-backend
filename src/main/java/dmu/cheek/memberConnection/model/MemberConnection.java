package dmu.cheek.memberConnection.model;

import dmu.cheek.global.auditing.BaseTimeEntity;
import dmu.cheek.member.model.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MemberConnection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberConnectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", foreignKey = @ForeignKey(name = "toMember", value = ConstraintMode.NO_CONSTRAINT))
    private Member toMember; //요청받는 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", foreignKey = @ForeignKey(name = "fromMember", value = ConstraintMode.NO_CONSTRAINT))
    private Member fromMember; //요청한 회원

    @Builder(builderMethodName = "allFields")
    public MemberConnection(long memberConnectionId, Member toMember, Member fromMember) {
        this.memberConnectionId = memberConnectionId;
        this.toMember = toMember;
        this.fromMember = fromMember;
    }

    @Builder(builderMethodName = "withoutPrimaryKey")
    public MemberConnection(Member toMember, Member fromMember) {
        this.toMember = toMember;
        this.fromMember = fromMember;
    }
}
