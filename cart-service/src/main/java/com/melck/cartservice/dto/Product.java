package com.melck.cartservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private Long id;
    private String name;
    private String skuCode;
    private String description;
    private BigDecimal price;
    private String imgUrl;
    private String coverImg;
    private Double rate;
    private Integer qtyReviews;
    private Boolean isInSale;
    private BigDecimal promotionalPrice;
    private Integer discountValue;
    List<CategoryDTO> categories = new ArrayList<>();
}
