package com.ra.controller;

import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import com.ra.repository.Impl.RepositoryImpl;
import com.ra.service.Impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "productServlet", value = "/products")
public class ProductServlet extends Controller {
    private RepositoryImpl<Product> productRepository;
    private ProductServiceImpl productService;

    @Override
    public void init() throws ServletException {
        this.productRepository = new RepositoryImpl<>();
        this.productService = new ProductServiceImpl();
    }


    public void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pageSize = 10;
        int page = Integer.parseInt(req.getParameter("page") == null ? "1" : req.getParameter("page"));

        String key = req.getParameter("key");
        List<Product> allProducts = productRepository.findAll(Product.class);
        List<Product> products = productRepository.findAllByPagination(Product.class, pageSize, page);
        // Tìm kiếm
        if (key != null) {
            allProducts = productRepository.findRelativeByIndexes(Product.class, key);
            products = productRepository.findByIndexesPagination(Product.class, pageSize, page, key);
        }
        // Phân trang
        int totalProducts = allProducts.size();
        int totalPages = (int) Math.ceil(totalProducts / (double) pageSize);

        req.setAttribute("totalProducts", totalProducts);
        req.setAttribute("products", products);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("page", page);
        req.setAttribute("key", key);

        req.getRequestDispatcher("views/products/index.jsp").forward(req, resp);
    }

    public void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        try {
            Product product = new Product();
            product.setProductId(req.getParameter("productId"));
            product.setProductName(req.getParameter("productName"));
            product.setManufacturer(req.getParameter("manufacturer"));
            product.setBatch(Integer.parseInt(req.getParameter("productBatch") == null ? "0" : req.getParameter("productBatch")));
            product.setProductStatus(Boolean.parseBoolean(req.getParameter("productStatus") == null ? "false" : req.getParameter("productStatus")));
            product.setQuantity(0);
            product.setCreated(new Date());
            product = productService.create(product);
            if (product != null) {
                req.setAttribute("product", product);
                req.setAttribute("warning", "tạo thành công");
            } else {
                req.setAttribute("warning", "tạo thất bại");
            }
        }catch (Exception e){
            req.setAttribute("warning", "tạo thất bại");
            e.printStackTrace();
        }
        req.getRequestDispatcher("views/products/create.jsp").forward(req, resp);
    }

    public void postCreate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("views/products/create.jsp").forward(req, resp);
    }
    public void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        try{
            String productId = req.getParameter("id");
            req.getSession().setAttribute("productId",String.valueOf(productId));
            Product editProduct = productRepository.findId(Product.class,productId);
            if (editProduct != null) {
                req.setAttribute("productId",editProduct.getProductId());
                req.setAttribute("editName", editProduct.getProductName());
                req.setAttribute("editManufacturer", editProduct.getManufacturer());
                req.setAttribute("editBatch", editProduct.getBatch());
                req.setAttribute("editStatus", editProduct.getProductStatus());
            } else {
                req.getRequestDispatcher("views/products/index.jsp").forward(req, resp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        req.getRequestDispatcher("views/products/edit.jsp").forward(req, resp);
    }

    public void postEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        try{
            String productId = req.getSession().getAttribute("productId").toString();
            Product editProduct = productRepository.findId(Product.class,productId);
            if (editProduct != null) {
                editProduct.setProductName(req.getParameter("productName"));
                editProduct.setManufacturer(req.getParameter("manufacturer"));
                editProduct.setBatch(Integer.parseInt(req.getParameter("productBatch")));
                editProduct.setProductStatus(Boolean.parseBoolean(req.getParameter("productStatus")));
                editProduct.setCreated(new Date());
                Product editedProduct = productService.update(editProduct);
                if (editedProduct != null){
                    req.setAttribute("key",editedProduct.getProductName());
                    index(req,resp);
                } else {
                    req.setAttribute("warning", " cập nhật thất bại");
                    req.getRequestDispatcher("views/products/edit.jsp").forward(req, resp);
                }
            } else {
                req.getRequestDispatcher("views/products/index.jsp").forward(req, resp);
            }
        }catch (Exception e){
            req.setAttribute("warning", " cập nhật thất bại");
            req.getRequestDispatcher("views/products/edit.jsp").forward(req, resp);
            e.printStackTrace();
        }
    }
}
