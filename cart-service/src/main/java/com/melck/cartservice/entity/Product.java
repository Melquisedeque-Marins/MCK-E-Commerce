package com.melck.cartservice.entity;

import jakarta.persistence.Column;
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
    private double price;
    private Integer quantity;
    private List<String> imgUrl;
}
