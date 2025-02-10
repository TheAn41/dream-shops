package com.thean.dreamshops.service.image;

import com.thean.dreamshops.dto.ImageDTO;
import com.thean.dreamshops.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDTO> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file,Long imageId);

}
