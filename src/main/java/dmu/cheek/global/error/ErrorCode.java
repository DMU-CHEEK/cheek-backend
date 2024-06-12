package dmu.cheek.global.error;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "business-exception-test"),

    //Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M-001", "member not found"),

    //Question
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Q-001", "question not found"),

    //Category
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "category not found")
    ;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;
}
