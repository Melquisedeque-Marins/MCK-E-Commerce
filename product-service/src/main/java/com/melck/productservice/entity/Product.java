package com.melck.productservice.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "tb_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String skuCode;
    @Column(columnDefinition = "TEXT")
    private String description;
    private BigDecimal price;
    @Column(columnDefinition = "TEXT")
    private List<String> imgUrl;
    private Double rate;
    private Integer qtyReviews;
}
