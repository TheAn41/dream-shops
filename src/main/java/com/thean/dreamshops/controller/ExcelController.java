package com.thean.dreamshops.controller;

import com.thean.dreamshops.model.Product;
import com.thean.dreamshops.service.product.ProductService;
import com.thean.dreamshops.util.excel.BaseExport;
import com.thean.dreamshops.util.excel.UploadExcel;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/excels")
public class ExcelController {
    @Autowired
    private final ProductService productService;


    private final UploadExcel uploadExcel;


    @GetMapping("/product/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=product_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".xlsx");

        List<Product> listProducts = productService.getAllProducts();

        BaseExport<Product> excelExporter = new BaseExport<>();

        excelExporter.createSheet("Product List", listProducts)
                .writeTitle("DANH SÁCH SẢN PHẨM", 6)
                .writeHeaderLine(new String[]{"ID", "Tên Sản Phẩm", "Thương hiệu", "Giá sản phẩm", "Số lượng", "Mô tả"})
                .writeDataLine(new String[]{"id", "name", "brand", "price", "inventory", "description"}, Product.class)
                .export(response);

    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty!");
            }
            List<List<String>> excelData = uploadExcel.readFileExcel(file);
            return ResponseEntity.ok(excelData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading Excel file: " + e.getMessage());
        }
    }

}
