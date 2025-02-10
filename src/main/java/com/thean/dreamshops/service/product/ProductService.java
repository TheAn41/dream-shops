package com.thean.dreamshops.service.product;

import com.thean.dreamshops.dto.ImageDTO;
import com.thean.dreamshops.dto.ProductDTO;
import com.thean.dreamshops.exception.NotFoundException;
import com.thean.dreamshops.model.Category;
import com.thean.dreamshops.model.Image;
import com.thean.dreamshops.model.Product;
import com.thean.dreamshops.repository.CategoryRepository;
import com.thean.dreamshops.repository.ImageRepository;
import com.thean.dreamshops.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(ProductDTO request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }
    private Product createProduct(ProductDTO request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product updateProduct(ProductDTO request, Long id) {

        return productRepository.findById(id)
                .map(existingProduct->updateExistingProduct(existingProduct,request))
                .map(productRepository::save)
                .orElseThrow(()->new NotFoundException("Product not found"));
    }
    private Product updateExistingProduct(Product existingProduct, ProductDTO request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new NotFoundException("Product not found");
        });
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
@Override
public List<ProductDTO> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDTO).toList();

    }


@Override
public ProductDTO convertToDTO(Product product) {
       ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
       List<Image> images = imageRepository.findByProductId(product.getId());
       List<ImageDTO> imageDTOS = images.stream().map(image -> modelMapper.map(image, ImageDTO.class)).toList();
       productDTO.setImages(imageDTOS);
       return productDTO;
    }
}
