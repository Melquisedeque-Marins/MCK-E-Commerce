package com.melck.productservice.dto;

import com.melck.productservice.entity.Category;
import com.melck.productservice.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse implements Serializable {
    private Long id;
    private String name;
    private String categoryImg;
    public CategoryResponse (Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.categoryImg = category.getCategoryImg();
    }

}
