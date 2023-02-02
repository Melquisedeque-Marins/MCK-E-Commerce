package com.melck.cartservice.dto;

import com.melck.cartservice.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private String name;
    private String skuCode;
    private String description;
    private double rate;
    private double price;
    private List<String> imgUrl;

    public ProductDTO (Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.rate = product.getRate();
        this.skuCode = product.getSkuCode();
        this.imgUrl = product.getImgUrl();
    }
}
