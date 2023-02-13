package com.melck.productservice.dto;

import com.melck.productservice.entity.Product;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private String skuCode;
    private String description;
    private double price;
    private List<String> imgUrl;
    private double rate;
    private Integer qtyReviews;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.skuCode = product.getSkuCode();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imgUrl = product.getImgUrl();
    }
}
