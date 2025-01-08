package com.thean.dreamshops.service;

import com.thean.dreamshops.dto.ProductDTO;
import com.thean.dreamshops.model.Product;


import java.util.List;

public interface IProductService {
    Product addProduct(ProductDTO request);
    Product updateProduct(ProductDTO request, Long id);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand,String name);
    Long countProductsByBrandAndName(String brand,String name);

}
