package com.thean.dreamshops.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
@Data
public class CartDTO implements Serializable {
    private Long cartId;
    private Set<CartItemDTO> items;
    private BigDecimal totalAmount;
}
