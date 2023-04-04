package com.melck.productservice.services;

import com.melck.productservice.entity.Category;
import com.melck.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        List<Category> categories = repository.findAll();
        log.info("Searching for categories...");
        return categories;
    }
}
