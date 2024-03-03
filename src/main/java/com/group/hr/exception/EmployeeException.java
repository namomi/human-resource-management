package com.group.hr.exception;

import com.group.hr.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public EmployeeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
