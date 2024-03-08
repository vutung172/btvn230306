package com.ra.service.Impl;

import com.ra.entity.Category;
import com.ra.repository.Impl.RepositoryImpl;
import com.ra.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private RepositoryImpl<Category> categoryRepository;

    public CategoryServiceImpl() {
        this.categoryRepository =  new RepositoryImpl<>();
    }

    @Override
    public List<Category> findAllCategory() {
        List<Category> categories = new ArrayList<>();
        try{
            categories = categoryRepository.findAll(Category.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.addIgnoreId(category);
    }
}
