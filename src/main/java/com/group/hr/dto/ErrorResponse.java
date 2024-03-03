package com.group.hr.dto;

import com.group.hr.type.ErrorCode;
import lombok.*;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
