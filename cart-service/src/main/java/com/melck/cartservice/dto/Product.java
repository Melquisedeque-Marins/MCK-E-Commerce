package com.melck.cartservice.dto;

import jakarta.persistence.Column;
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
public class Product {

    private Long id;
    private String name;
    private String skuCode;
    private String description;
    private double rate;
    private BigDecimal price;
    private List<String> imgUrl;
    private Integer quantity;
}
