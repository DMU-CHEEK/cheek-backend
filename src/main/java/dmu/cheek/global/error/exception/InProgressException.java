package dmu.cheek.global.error.exception;

import dmu.cheek.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InProgressException extends RuntimeException{

    private final ErrorCode errorCode;

}
