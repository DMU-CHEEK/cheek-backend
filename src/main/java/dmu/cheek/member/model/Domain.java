package dmu.cheek.member.model;

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
    private long domainId;

    private String domain;

    private boolean isValid;

    @Builder
    public Domain(String domain, boolean isValid) {
        this.domain = domain;
        this.isValid = isValid;
    }
}
