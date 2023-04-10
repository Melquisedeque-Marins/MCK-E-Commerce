package com.melck.productservice.services;

import com.melck.productservice.entity.Category;
import com.melck.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository repository;

    public List<Category> findAllCats() {
        return repository.findAll();
    }
}
