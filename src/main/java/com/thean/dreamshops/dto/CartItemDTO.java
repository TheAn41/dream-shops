package com.thean.dreamshops.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class CartItemDTO implements Serializable {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDTO product;
}
