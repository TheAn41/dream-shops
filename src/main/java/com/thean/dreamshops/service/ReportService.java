package com.thean.dreamshops.service;

import com.thean.dreamshops.dto.ReportDTO;
import com.thean.dreamshops.model.Report;
import com.thean.dreamshops.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
private final ReportRepository reportRepository;



    public byte[] generateAnalysisReport() {
        try {
            //Lấy du lieu
            List<Report> data = reportRepository.findAll();

            // Tạo workbook và sheet
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Báo cáo");

            // Font và style tiêu đề chính
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleFont.setFontName("Times New Roman");
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Font và style tiêu đề bảng
            Font headerFont = workbook.createFont();
            headerFont.setBold(true); // In đậm
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setFontName("Times New Roman");
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setWrapText(true); // Đảm bảo văn bản được xuống dòng trong ô
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);


            // Tạo dòng thông tin đơn vị
            Row infoRow = sheet.createRow(0);
            infoRow.createCell(0).setCellValue("Đơn vị: "+ data.size());
            infoRow.createCell(6).setCellValue("Mẫu số SXKD");
            Row infoRow1 = sheet.createRow(1);
            infoRow1.createCell(0).setCellValue("Mã QHNS: "+data.size());

            // Tạo tiêu đề chính
            Row titleRow = sheet.createRow(2);
            titleRow.setHeightInPoints(25);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("SỔ CHI TIẾT CHI PHÍ HOẠT ĐỘNG KINH DOANH DỊCH VỤ PHÁT SINH");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 6));

            CellStyle borderedStyle = workbook.createCellStyle();

            borderedStyle.setAlignment(HorizontalAlignment.CENTER);
            borderedStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            borderedStyle.setBorderTop(BorderStyle.THIN);  // Thêm viền trên
            borderedStyle.setBorderBottom(BorderStyle.THIN);
            borderedStyle.setBorderLeft(BorderStyle.THIN);
            borderedStyle.setBorderRight(BorderStyle.THIN);

            // Tạo dòng tiêu đề bảng
            Row headerRow1 = sheet.createRow(5);
            String[] mainHeaders = {"Nội dung chi phí","Tổng chi phí", "Chi phí quản lý", "Chi tết chi phí"};
            int[] mainHeaderMerge = {1,2,2,2};
            sheet.addMergedRegion(new CellRangeAddress(5, 6, 0, 0));
            int colIndex = 0;
            for (int i = 1; i < 7; i++) {
                Cell cell = headerRow1.createCell(i);
                cell.setCellStyle(headerStyle);
            }

            for (int i = 0 ; i < mainHeaders.length; i++) {
                Cell cell = headerRow1.createCell(colIndex);

                if (mainHeaderMerge[i] > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(5, 5, colIndex, colIndex + mainHeaderMerge[i] - 1));
                }

                cell.setCellValue(mainHeaders[i]);
                cell.setCellStyle(headerStyle);

                colIndex += mainHeaderMerge[i];
            }

            // Tạo dòng tiêu đề phụ
            Row headerRow2 = sheet.createRow(6);
            String[] subHeaders = {"", "Kỳ này", "Luỹ kế từ đầu năm", "Kỳ này", "Luỹ kế từ đầu năm", "Kỳ này", "Luỹ kế từ đầu năm"};
            for (int i = 0; i < subHeaders.length; i++) {
                Cell cell = headerRow2.createCell(i);
                cell.setCellValue(subHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            Row headerRow3 = sheet.createRow(7);
            String[] subHeaders1 = {"A", "B", "C", "D", "E", "1", "2"};
            for (int i = 0; i < subHeaders1.length; i++) {
                Cell cell = headerRow3.createCell(i);
                cell.setCellValue(subHeaders1[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowCount = 8;
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(rowCount++);
                Report item = data.get(i);

                // Các ô dữ liệu
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(item.getNoiDungChiPhi() != null ? item.getNoiDungChiPhi() : "");
                cell0.setCellStyle(borderedStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(String.valueOf(item.getTongChiPhi_KyNay() != null ? item.getTongChiPhi_KyNay() : ""));
                cell1.setCellStyle(borderedStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(String.valueOf(item.getTongChiPhi_LuyKeTuDauNam() != null ? item.getTongChiPhi_LuyKeTuDauNam() : "null"));
                cell2.setCellStyle(borderedStyle);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(String.valueOf(item.getChiPhiQuanLy_KyNay() != null ? item.getChiPhiQuanLy_KyNay() : "null"));
                cell3.setCellStyle(borderedStyle);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(String.valueOf(item.getChiPhiQuanLy_LuyKeTuDauNam() != null ? item.getChiPhiQuanLy_LuyKeTuDauNam() : "null"));
                cell4.setCellStyle(borderedStyle);

                Cell cell5 = row.createCell(5);
                cell5.setCellValue(String.valueOf(item.getChiTietChiPhi_KyNay() != null ? item.getChiTietChiPhi_KyNay() : "null"));
                cell5.setCellStyle(borderedStyle);

                Cell cell6 = row.createCell(6);
                cell6.setCellValue(String.valueOf(item.getChitietChiPhi_LuyKeTuDauNam() != null ? item.getChitietChiPhi_LuyKeTuDauNam() : "null"));
                cell6.setCellStyle(borderedStyle);

            }
            // Thêm hàng tổng
            Row totalRow = sheet.createRow(rowCount++);
            Cell totalCell = totalRow.createCell(0);
            totalCell.setCellValue("Cộng");
            totalCell.setCellStyle(borderedStyle);





        Cell totalTongChiPhiKyNay = totalRow.createCell(1);
            totalTongChiPhiKyNay.setCellValue(data.stream()
                    .map(item -> item.getTongChiPhi_KyNay()!= null ? item.getTongChiPhi_KyNay() : 0.0)
                    .reduce(0.0,Double::sum));
            totalTongChiPhiKyNay.setCellStyle(borderedStyle);

        Cell totalTongChiPhiLuyKeTuDauNam = totalRow.createCell(2);
            totalTongChiPhiLuyKeTuDauNam.setCellValue(data.stream()
                    .map(item-> item.getTongChiPhi_LuyKeTuDauNam() != null ? item.getTongChiPhi_LuyKeTuDauNam() : 0.0)
                    .reduce(0.0,Double::sum));
            totalTongChiPhiLuyKeTuDauNam.setCellStyle(borderedStyle);

            Cell totalChiPhiQuanLyKyNay = totalRow.createCell(3);
            totalChiPhiQuanLyKyNay.setCellValue(data.stream()
                    .map(item -> item.getChiPhiQuanLy_KyNay()!= null ? item.getChiPhiQuanLy_KyNay() : 0.0)
                    .reduce(0.0,Double::sum));
            totalChiPhiQuanLyKyNay.setCellStyle(borderedStyle);

            Cell totalChiPhiQuanLyLuyKeTuDauNam = totalRow.createCell(4);
            totalChiPhiQuanLyLuyKeTuDauNam.setCellValue(data.stream()
                    .map(item-> item.getChiPhiQuanLy_LuyKeTuDauNam() != null ? item.getChiPhiQuanLy_LuyKeTuDauNam() : 0.0)
                    .reduce(0.0,Double::sum));
            totalChiPhiQuanLyLuyKeTuDauNam.setCellStyle(borderedStyle);

            Cell totalChiTietChiPhiKyNay = totalRow.createCell(5);
            totalChiTietChiPhiKyNay.setCellValue(data.stream()
                    .map(item -> item.getChiTietChiPhi_KyNay()!= null ? item.getChiTietChiPhi_KyNay() : 0.0)
                    .reduce(0.0,Double::sum));
            totalChiTietChiPhiKyNay.setCellStyle(borderedStyle);

            Cell totalChiTietChiPhiLuyKeTuDauNam = totalRow.createCell(6);
            totalChiTietChiPhiLuyKeTuDauNam.setCellValue(data.stream()
                    .map(item-> item.getChitietChiPhi_LuyKeTuDauNam() != null ? item.getChitietChiPhi_LuyKeTuDauNam() : 0.0)
                    .reduce(0.0,Double::sum));
            totalChiTietChiPhiLuyKeTuDauNam.setCellStyle(borderedStyle);


            int key = rowCount++ +1;

            Row signKey = sheet.createRow( key );
            Cell signCell = signKey.createCell(0);
            signCell.setCellValue("NGƯỜI LẬP SỔ");
            sheet.addMergedRegion(new CellRangeAddress(key, key,0,2));


            Cell signCell1 = signKey.createCell(3);
            signCell1.setCellValue("KẾ TOÁN TRƯỞNG");
            sheet.addMergedRegion(new CellRangeAddress(key, key,3,6));

            Row signKey1 = sheet.createRow( key + 1 );
            Cell signCell2 = signKey1.createCell(0);
            signCell2.setCellValue("(Ký, họ tên)");
            sheet.addMergedRegion(new CellRangeAddress(key +1, key+1,0,2));


            Cell signCell3 = signKey1.createCell(3);
            signCell3.setCellValue("(Ký, họ tên)");
            sheet.addMergedRegion(new CellRangeAddress(key+1, key+1,3,6));



            // Thiết lập độ rộng cột tự động
            for (int i = 0; i <= 6; i++) {
                sheet.autoSizeColumn(i);
            }

            // Xuất file Excel ra mảng byte
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            } finally {
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }



}
