package com.melck.productservice.dto;

import com.melck.productservice.entity.Product;
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
public class ProductRequest {

    private String name;
    private String description;
    private double rate;
    private double price;
    private List<String> imgUrl;

    public ProductRequest(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.rate = product.getRate();
        this.price = product.getPrice();
        this.imgUrl = product.getImgUrl();

    }
}
