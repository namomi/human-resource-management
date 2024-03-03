package com.group.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AttendanceController.class)
class AttendanceControllerTest {

    @MockBean
    AttendanceController attendanceController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Test
    public void successAttendance() throws Exception {
        Long employeeId = 1L;

        // 출근 처리를 위한 POST 요청 시뮬레이션
        mockMvc.perform(post("/employee/attendance/" + employeeId))
                .andExpect(status().isOk());
    }

    @Test
    public void successGotOffWork() throws Exception {
        Long employeeId = 1L;

        mockMvc.perform(post("/employee/gotOffWork/" + employeeId))
                .andExpect(status().isOk());
    }

    @Test
    public void successGetMonthlyAttendance() throws Exception {
        Long employeeId = 1L;
        String date = "2024-01";

        mockMvc.perform(get("/employee/attendance/" + employeeId + "/" + date))
                .andExpect(status().isOk());
    }
}
