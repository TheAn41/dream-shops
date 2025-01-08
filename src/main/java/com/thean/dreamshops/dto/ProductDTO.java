package com.thean.dreamshops.dto;

import com.thean.dreamshops.model.Category;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProductDTO implements Serializable {
    private Long id;

    private String name;

    private String brand;

    private double price;

    private int inventory;

    private String description;

    private Category category;
}
