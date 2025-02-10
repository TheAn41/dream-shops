package com.thean.dreamshops.service.category;

import com.thean.dreamshops.dto.CategoryDTO;
import com.thean.dreamshops.exception.AlredyExistingException;
import com.thean.dreamshops.exception.NotFoundException;
import com.thean.dreamshops.model.Category;
import com.thean.dreamshops.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new NotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(CategoryDTO category) {
        return Optional.of(category).filter(c->!categoryRepository.existsByName(c.getName()))
                .map(this::convertToCategory)
                .map(categoryRepository :: save)
                .orElseThrow(()->new AlredyExistingException(category.getName()+" already exists"))
                ;
    }
    private Category convertToCategory(CategoryDTO categoryDTO) {
        return new Category(categoryDTO.getName());
    }



    @Override
    public Category updateCategory(CategoryDTO category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory->{
            boolean exists = categoryRepository.existsByName(category.getName());
            if (exists) {
                throw new AlredyExistingException("Category name already exists");
            }
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(()->new NotFoundException("Category not found"));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
            throw new NotFoundException("Category not found");
        });
    }
}
