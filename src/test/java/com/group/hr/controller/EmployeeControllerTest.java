package com.group.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.hr.dto.EmployeeDto;
import com.group.hr.service.AttendanceService;
import com.group.hr.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.group.hr.type.Role.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private AttendanceService attendanceService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void successCreateEmployee() throws Exception{
        //given
        EmployeeDto employeeDto = new EmployeeDto("하나", "stella", MEMBER,
                LocalDate.parse("1997-02-01"), LocalDate.parse("2024-02-01"));
        given(employeeService.save(employeeDto))
                .willReturn(EmployeeDto.builder()
                        .name("하나")
                        .teamName("stella")
                        .role(MEMBER)
                        .birthday(LocalDate.parse("1997-02-01"))
                        .workStartDate(LocalDate.parse("2024-02-01"))
                        .build());

        //when
        //then
        mockMvc.perform(post("/employee/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk());

    }

    @Test
    public void testAssignTeam() throws Exception {
        //given
        Long employeeId = 1L;
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setTeamName("spring");
        //when
        doNothing().when(employeeService).assignEmployeeToTeam(eq(employeeId), any(EmployeeDto.class));

        //then
        mockMvc.perform(put("/employee/{employeeId}/team", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void successFindEmployee() throws Exception{
        //given
        List<EmployeeDto> employee = Arrays.asList(
                new EmployeeDto("히나", "stella", MEMBER, LocalDate.parse("1997-02-01"), LocalDate.parse("2023-05-01")),
                new EmployeeDto("시로", "stella", MEMBER, LocalDate.parse("1997-03-01"), LocalDate.parse("2023-03-01")),
                new EmployeeDto("타비", "stella", MEMBER, LocalDate.parse("1997-04-01"), LocalDate.parse("2023-02-07")),
                new EmployeeDto("칸나", "stella", MEMBER, LocalDate.parse("1997-05-01"), LocalDate.parse("2023-01-01"))
        );
        PageImpl<EmployeeDto> employeeDtoPage = new PageImpl<>(employee);
        PageRequest pageable = PageRequest.of(0, 10);

        given(employeeService.getAllEmployee(pageable)).willReturn(employeeDtoPage);

        //when
        Page<EmployeeDto> result = employeeService.getAllEmployee(pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(employee.size());
    }



}