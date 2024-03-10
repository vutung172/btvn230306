package com.ra.model.entity;

import com.ra.util.Annotation.Column;
import com.ra.util.Annotation.Id;
import com.ra.util.Annotation.Table;

@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "CategoryID")
    private int categoryId;
    @Column(name = "CategoryName")
    private String categoryName;
    @Column(name = "Description")
    private String description;
    @Column(name = "ParentCategoryID")
    private int parentCategoryId;

    public Category() {
    }

    public Category(int categoryId, String categoryName, String description, int parentCategoryId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
