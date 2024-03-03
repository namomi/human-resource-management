package com.group.hr.exception;

import com.group.hr.type.ErrorCode;
import lombok.*;

import java.text.MessageFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnualLeaveException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public AnnualLeaveException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    public AnnualLeaveException(ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getDescription(), args));
        this.errorCode = errorCode;
        this.errorMessage = String.format(errorCode.getDescription(), args);
    }

}
