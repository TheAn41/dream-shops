package com.thean.dreamshops.controller;

import com.thean.dreamshops.model.Report;
import com.thean.dreamshops.service.ReportService;
import com.thean.dreamshops.util.excel.BaseResponse;
import com.thean.dreamshops.dto.ReportDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/download-excel-analysis-report")
    public ResponseEntity<?> downloadExcelAnalysisReport() {
        try {
            byte[] data = reportService.generateAnalysisReport();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "analysis_report.xlsx");
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse("ER:5", "Có lỗi xảy ra"));
        }
    }
}
