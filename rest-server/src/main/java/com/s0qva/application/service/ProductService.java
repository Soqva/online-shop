package com.s0qva.application.service;

import com.s0qva.application.exception.NoSuchProductException;
import com.s0qva.application.exception.UnsavedProductHasIdException;
import com.s0qva.application.model.Product;
import com.s0qva.application.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new NoSuchProductException("There is no product with id = " + id));
    }

    public Long saveProduct(Product product) {
        if (product.getId() != null) {
            throw new UnsavedProductHasIdException("It is wrong that unsaved product has id = " + product.getId());
        }

        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    public Product updateProduct(Long id, Product product) {
        Product oldProduct = getProduct(id);

        oldProduct.setName(product.getName());
        oldProduct.setPrice(product.getPrice());
        oldProduct.addDetails(product.getDetails());

        return productRepository.save(oldProduct);
    }

    public void deleteProduct(Long id) {
        Product product = getProduct(id);
        productRepository.delete(product);
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
