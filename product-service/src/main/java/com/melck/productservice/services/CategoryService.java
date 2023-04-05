package com.melck.productservice.services;

import com.melck.productservice.dto.CategoryResponse;
import com.melck.productservice.entity.Category;
import com.melck.productservice.repository.CategoryRepository;
import com.melck.productservice.services.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = repository.findAll();
        log.info("Searching for categories...");
        return categories.stream().map(cat -> new CategoryResponse(cat)).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        Category category = repository.findById(id).orElseThrow( () -> new ProductNotFoundException("adgagadsg"));
        log.info("Searching for categories...");
        return new CategoryResponse(category);
    }
}
