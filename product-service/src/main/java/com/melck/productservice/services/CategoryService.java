package com.melck.productservice.services;

import com.melck.productservice.entity.Category;
import com.melck.productservice.repository.CategoryRepository;
import com.melck.productservice.services.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository repository;

    public List<Category> findAllCats() {
        return repository.findAll();
    }

    public Category findCategoryById(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Product with id: " + id + " not found");
            return new ProductNotFoundException("Product with id: " + id + " not found");
        });
    }
}
