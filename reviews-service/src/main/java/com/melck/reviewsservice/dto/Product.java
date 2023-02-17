package com.melck.reviewsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private double rate;
    private Integer qtyReviews;
    private double price;
    private List<String> imgUrl;
}
