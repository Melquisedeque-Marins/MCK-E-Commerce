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
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private String skuCode;
    private String description;
    private BigDecimal price;
    private String imgUrl;
    private String coverImg;
    private double rate;
    private Integer qtyReviews;
    private List<Category> categories = new ArrayList<>();

    public static ProductResponse of(Product product) {
        log.info("products from db {}", product);

        var response = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .skuCode(product.getSkuCode())
                .description(product.getDescription())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .coverImg(product.getCoverImg())
                .rate(product.getRate())
                .qtyReviews(product.getQtyReviews())
                .build();
        List<Category> cats = product.getCategories().stream().toList();
        response.setCategories(cats);
        return response;
    }
//    public static ProductResponse of(Product product, Set<Category> categories) {
//        var response = ProductResponse.of(product);
//        response.getCategories().addAll(product.getCategories().stream().map(CategoryResponse::new).toList());
//        log.info("testando {}", response);
//        return response;
//    }
}
