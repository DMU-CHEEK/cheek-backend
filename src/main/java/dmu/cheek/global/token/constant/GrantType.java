package dmu.cheek.global.token.constant;

import lombok.Getter;

@Getter
public enum GrantType {

    BEARER("Bearer");

    GrantType(String grantType) {
        this.grantType = grantType;
    }

    private String grantType;
}
