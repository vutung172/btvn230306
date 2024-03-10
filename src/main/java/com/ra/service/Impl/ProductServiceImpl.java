package com.ra.service.Impl;

import com.ra.model.entity.Product;
import com.ra.repository.Impl.RepositoryImpl;
import com.ra.service.GeneralService;

import java.util.List;

public class ProductServiceImpl implements GeneralService<Product> {
    private RepositoryImpl<Product> productRepository;

    public ProductServiceImpl() {
        this.productRepository = new RepositoryImpl<>();
    }

    @Override
    public List<Product> listAll() {
        return null;
    }

    @Override
    public Product create(Product product) {
        return productRepository.add(product);
    }

    @Override
    public Product update(Product newProduct) {
        return productRepository.edit(newProduct);
    }

    @Override
    public List<Product> search(String any) {
        return null;
    }
}
