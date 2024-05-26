package dmu.cheek.api.member.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "domain_id")
    private long domainId;

    private String domain;

    @Builder
    public void Domain(String domain) {
        this.domain = domain;
    }
}
