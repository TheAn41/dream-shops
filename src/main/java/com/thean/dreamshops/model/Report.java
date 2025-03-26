package com.thean.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String noiDungChiPhi;
    private Double tongChiPhi_KyNay;
    private Double tongChiPhi_LuyKeTuDauNam;
    private Double chiPhiQuanLy_KyNay;
    private Double chiPhiQuanLy_LuyKeTuDauNam;
    private Double chiTietChiPhi_KyNay;
    private Double chitietChiPhi_LuyKeTuDauNam;

}

