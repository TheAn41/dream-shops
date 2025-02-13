package com.thean.dreamshops.dto;

import com.thean.dreamshops.model.Category;
import com.thean.dreamshops.model.Image;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO implements Serializable {
    private Long id;

    private String name;

    private String brand;

    private BigDecimal price;

    private int inventory;

    private String description;

    private Category category;

    private List<ImageDTO> images;
}
