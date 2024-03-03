package com.group.hr.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    EMPLOYEE_NOT_FOUND("해당 직원이 없습니다."),
    EMPLOYEE_ALREADY_EXIST("해당 직원이 이미 등록되었습니다."),
    EMPLOYEE_ALREADY_ATTENDANCE("해당 직원이 이미 출근했습니다."),
    EMPLOYEE_ALREADY_GOT_OFF_WORK("직원은 이미 퇴근했습니다."),
    NO_ATTENDANCE_RECORD_TODAY("오늘의 출근 기록이 없습니다."),
    TEAM_NOT_FOUND("해당 팀이 없습니다."),
    TEAM_ALREADY_EXIST("해당 팀은 이미 있습니다."),
    NO_ANNUAL_LEAVE_RECORD("연차 기록이 없습니다"),
    ANNUAL_LEAVE_APPLICATION_ADVANCE("연차 신청은 최소 {0} 일 전에 해야 합니다."),
    ANNUAL_LEAVE_ALREADY_USED("해당 날짜에 연차를 이미 사용하였습니다."),
    EXCEEDED_ANNUAL_LEAVES("요청된 연차 수가 사용 가능한 연차 수를 초과합니다.");


    private final String description;
}
