package dmu.cheek.global.token;

public enum TokenType {

    ACCESS, REFRESH;

    public static boolean isAccessType(String tokenType) {

        return TokenType.ACCESS.name().equals(tokenType);
    }
}
