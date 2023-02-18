package com.melck.reviewsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String skuCode;
    private String description;
    private BigDecimal price;
    private List<String> imgUrl;
    private double rate;
    private Integer qtyReviews;
}
