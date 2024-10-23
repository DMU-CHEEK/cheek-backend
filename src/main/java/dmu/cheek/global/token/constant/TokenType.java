package dmu.cheek.global.token.constant;

public enum TokenType {

    ACCESS, REFRESH;

    public static boolean isAccessType(String tokenType) {
        return TokenType.ACCESS.name().equals(tokenType);
    }
}
