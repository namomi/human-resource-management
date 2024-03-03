package com.group.hr.service;

import com.group.hr.dto.OvertimeDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final OvertimeService overtimeService;

    @Scheduled(cron = "0 0 6 1 * ?")
    public void generateMonthlyOvertimeReport() throws Exception {
        LocalDate previousMonth = LocalDate.now().minusMonths(1);
        int year = previousMonth.getYear();
        int month = previousMonth.getMonthValue();

        List<OvertimeDto> overtimeData = overtimeService.getMonthlyOvertime(year, month);

        // CSV 파일 생성
        String fileName = "overtime_report_" + year + "_" + month + ".csv";
        createCsvFile(fileName, overtimeData);
    }

    private void createCsvFile(String fileName, List<OvertimeDto> data) throws IOException {
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("ID", "Name", "Overtime Minutes"))
        ) {
            for (OvertimeDto dto : data) {
                csvPrinter.printRecord(dto.getId(), dto.getName(), dto.getOvertimeMinutes());
            }
            csvPrinter.flush();
        }
    }
}
