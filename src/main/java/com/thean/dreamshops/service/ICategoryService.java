package com.thean.dreamshops.service;

import com.thean.dreamshops.dto.CategoryDTO;
import com.thean.dreamshops.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(CategoryDTO category);
    Category updateCategory(CategoryDTO category,Long id);
    void deleteCategory(Long id);
}
