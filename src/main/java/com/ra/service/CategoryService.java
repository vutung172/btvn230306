package com.ra.service;

import com.ra.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAllCategory();
    Category addCategory(Category category);
}
