package com.melck.productservice.dto;

import com.melck.productservice.entity.Category;
import com.melck.productservice.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryResponse implements Serializable {
    private Long id;
    private String name;

    public CategoryResponse(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
