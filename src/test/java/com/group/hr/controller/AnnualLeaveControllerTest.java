package com.group.hr.controller;

import com.group.hr.service.AnnualLeaveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnnualLeaveController.class)
class AnnualLeaveControllerTest {

    @MockBean
    private AnnualLeaveService annualLeaveService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void successAttendance() throws Exception {
        // given
        Long employeeId = 1L;

        // when
        // than
        mockMvc.perform(post("/employee/attendance/" + employeeId))
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