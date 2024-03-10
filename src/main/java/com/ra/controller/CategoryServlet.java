package com.ra.controller;

import com.ra.model.entity.Category;
import com.ra.service.CategoryService;
import com.ra.service.Impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "categoryServlet", value = "/category")
public class CategoryServlet extends HttpServlet {
    private final CategoryService categoryService;

    public CategoryServlet() {
        this.categoryService = new CategoryServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        boolean display = false;
        display = Boolean.parseBoolean(req.getParameter("display"));
        req.setAttribute("display",display);
        if (display){
            List<Category> categories = categoryService.findAllCategory();
            req.setAttribute("txtCategories",categories);
        }
        req.getRequestDispatcher("views/categoryInput.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        try{
            Category category = new Category();
            category.setCategoryName(req.getParameter("categoryName"));
            category.setDescription(req.getParameter("description"));
            category.setParentCategoryId(Integer.parseInt(req.getParameter("parentCategoryId")));
            Category addCategory = categoryService.addCategory(category);
            if (addCategory != null){
               req.setAttribute("category",addCategory);
            }
            req.getRequestDispatcher("views/categoryInput.jsp").forward(req,resp);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
