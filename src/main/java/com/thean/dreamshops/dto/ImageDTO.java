package com.thean.dreamshops.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class ImageDTO implements Serializable {
    private  Long imageId;
    private String imageName;
    private String downloadUrl;
}
