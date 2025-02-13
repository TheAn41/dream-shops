package com.thean.dreamshops.util.excel;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class BaseExport<T> {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
   private List<T> listData;

   public BaseExport() {
       workbook = new XSSFWorkbook();
   }

   public BaseExport<T> createSheet(String sheetName,List<T> listData) {
        this.sheet = workbook.createSheet(sheetName);
        this.listData = listData;
        return this;
   }



    public BaseExport<T> writeTitle(String title, int columnSpan) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);

        CellStyle titleStyle = workbook.createCellStyle();
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeight(16);
        titleFont.setBold(true);
        titleFont.setFontName("Times New Roman");
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleStyle);

        // Merge cells for the title
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnSpan - 1));

        return this;
    }

   public BaseExport<T> writeHeaderLine(String[] header){
//       sheet = workbook.createSheet("Data Export");
       Row row = sheet.createRow(1);
       CellStyle style = workbook.createCellStyle();
       XSSFFont font = workbook.createFont();
       font.setBold(true);
       font.setFontHeight(12);
       font.setFontName("Times New Roman");
       font.setColor(IndexedColors.BLUE_GREY.getIndex());
       style.setFont(font);
       style.setBorderBottom(BorderStyle.THIN);
       style.setBorderLeft(BorderStyle.THIN);
       style.setBorderRight(BorderStyle.THIN);
       style.setBorderTop(BorderStyle.THIN);
       style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
       style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
       for (int i = 0; i < header.length; i++) {
            createCell(row,i,header[i],style);
       }
       return this;

   }

   private void createCell(Row row,int columnCount, Object value,CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer){
            cell.setCellValue((Integer)value);
        }else if (value instanceof Double){
            cell.setCellValue((Double)value);
        }else if (value instanceof Boolean){
            cell.setCellValue((Boolean)value);
        }else if (value instanceof Date){
            cell.setCellValue(value.toString());
        }else{
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(style);
   }

   public BaseExport<T> writeDataLine(String[] fields,Class<T> clazz){
       int rowCount = 2;
       CellStyle style = workbook.createCellStyle();
       XSSFFont font = workbook.createFont();

       font.setFontHeight(12);
       font.setFontName("Times New Roman");
       style.setFont(font);
       style.setBorderBottom(BorderStyle.THIN);
       style.setBorderLeft(BorderStyle.THIN);
       style.setBorderRight(BorderStyle.THIN);
       style.setBorderTop(BorderStyle.THIN);

       for (val data: listData ) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

                for(String fieldName : fields){
                    try{
                        Field field = clazz.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object value = field.get(data);
                        createCell(row,columnCount,value,style);
                    }catch (NoSuchFieldException | IllegalAccessException e){
                        e.printStackTrace();
                    }
                    columnCount++;
                }
       }
       return this;
   }
//Export Blob
   public void export(HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
       outputStream.close();
   }
//Export Base64
   public void exportBase64(HttpServletResponse response) throws IOException {
       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
       workbook.write(byteArrayOutputStream);
       workbook.close();
       byte[] byteArray = byteArrayOutputStream.toByteArray();
       byteArrayOutputStream.close();
       String base64EncodedString = Base64.getEncoder().encodeToString(byteArray);
       ServletOutputStream outputStream = response.getOutputStream();
       outputStream.write(base64EncodedString.getBytes());
       outputStream.close();
   }
}
