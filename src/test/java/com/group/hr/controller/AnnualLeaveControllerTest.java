package com.group.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.hr.dto.AnnualLeaveDto;
import com.group.hr.service.AnnualLeaveService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

@WebMvcTest(AnnualLeaveController.class)
@ExtendWith(MockitoExtension.class)
class AnnualLeaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnnualLeaveService annualLeaveService;

    @InjectMocks
    private AnnualLeaveController annualLeaveController;

    private ObjectMapper objectMapper;
    private AnnualLeaveDto annualLeaveDto;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        annualLeaveDto = new AnnualLeaveDto(1L, LocalDate.of(2024, 2, 27), 3);
    }


    @Test
    public void successAttendance() throws Exception {
        Long employeeId = 1L;

        doNothing().when(annualLeaveService).applyForLeave(employeeId, annualLeaveDto.getLeaveDate(), annualLeaveDto.getRequestedLeaves());

        mockMvc.perform(post("/employee/annual-leve/{employeeId}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(annualLeaveDto)))
            .andExpect(status().isOk());
    }

    @Test
    public void successTotalLeaves() throws Exception {
        Long employeeId = 1L;
        int expectedLeaves = 15;

        when(annualLeaveService.getTotalLeaves(employeeId)).thenReturn(expectedLeaves);

        mockMvc.perform(get("/employee/total-leaves/" + employeeId))
                .andExpect(status().isOk());

        verify(annualLeaveService).getTotalLeaves(employeeId);
    }
}
