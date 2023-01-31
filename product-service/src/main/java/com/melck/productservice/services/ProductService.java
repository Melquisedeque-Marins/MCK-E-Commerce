package com.melck.productservice.services;

import com.melck.productservice.entity.Product;
import com.melck.productservice.repository.ProductRepository;
import com.melck.productservice.services.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;

    public Product insert (Product product) {
        return repository.save(product);
    }

    public Product getProductById(Long id) {
        Optional<Product> product = repository.findById(id);
        return product.orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public List<Product> getAllProduct() {
        return repository.findAll();
    }


}
