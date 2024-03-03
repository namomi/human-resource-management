package com.group.hr.exception;

import com.group.hr.type.ErrorCode;
import lombok.*;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public TeamException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
