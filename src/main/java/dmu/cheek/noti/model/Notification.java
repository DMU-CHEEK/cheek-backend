package dmu.cheek.noti.model;

import dmu.cheek.member.model.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", nullable = false)
    private Member toMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", nullable = true)
    private Member fromMember;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    private long typeId;

    private String title;

    private String body;

    @Builder(builderMethodName = "withoutPrimaryKey")
    public Notification(Member toMember, Member fromMember, Type type, long typeId, String title, String body) {
        this.toMember = toMember;
        this.fromMember = fromMember;
        this.type = type;
        this.typeId = typeId;
        this.title = title;
        this.body = body;
    }
}
