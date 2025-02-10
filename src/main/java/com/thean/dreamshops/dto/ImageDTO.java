package com.thean.dreamshops.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class ImageDTO implements Serializable {
    private  Long id;
    private String fileName;
    private String downloadUrl;
}
