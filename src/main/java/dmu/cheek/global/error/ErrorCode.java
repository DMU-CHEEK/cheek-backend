package dmu.cheek.global.error;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "business-exception-test"),

    //Authentication & Authorization
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "A-001", "invalid token format"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "expired token"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-003", "invalid token"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "A-004", "token not found"),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-005", "not valid token"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A-006", "do not have permission to access this resource"),
    NOT_EXIST_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-007", "not exist authorization"),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-008", "not valid Bearer grant type"),

    //Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M-001", "member not found"),
    MENTEE_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "M-002", "mentee not available"),
    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, "M-003", "invalid member type"),

    //Member Connection
    CONNECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "member connection not found"),
    DUPLICATED_CONNECTION(HttpStatus.BAD_REQUEST, "C-002", "duplicated connection"),

    //Email Verification
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "E-001", "email not found"),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "E-002", "duplicated email"),
    EXPIRED_CODE(HttpStatus.BAD_REQUEST, "E-003", "validity period has expired"),
    CODE_NOT_MATCH(HttpStatus.BAD_REQUEST , "E-004", "verification code does not match"),
    CANNOT_READ_TEMPLATE(HttpStatus.NOT_FOUND, "E-005", "could not read template"),
    IN_PROGRESS(HttpStatus.OK, "E-006", "email registration request is progress"),

    //Domain
    DUPLICATED_DOMAIN(HttpStatus.BAD_REQUEST, "D-001", "already exist domain"),

    //Question
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Q-001", "question not found"),

    //Story
    STORY_NOT_FOUND(HttpStatus.NOT_FOUND, "S-001", "story not found"),

    //HIGHLIGHT
    HIGHLIGHT_NOT_FOUND(HttpStatus.NOT_FOUND, "H-001", "highlight not found"),

    //COLLECTION
    COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "collection not found"),

    //FOLDER
    FOLDER_NOT_FOUND(HttpStatus.NOT_FOUND, "F-001", "folder not found"),

    //Category
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "category not found"),

    //Image
    UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "I-001", "failed to upload image"),

    //Notification
    NOTIFICATION_SENDING_FAILED(HttpStatus.BAD_REQUEST, "N-001", "notification sending failed")
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
