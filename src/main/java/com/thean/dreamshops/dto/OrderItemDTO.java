package com.thean.dreamshops.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class OrderItemDTO implements Serializable {
    private Long productId;
    private String productName;
    private String productBrand;
    private String productDescription;
    private int quantity;
    private BigDecimal price;
}
