package com.melck.productservice.dto;

import com.melck.productservice.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class ProductRequest {

    @NotBlank(message = "This field is required")
    private String name;
    @NotBlank(message = "This field is required")
    private String skuCode;
    @NotBlank(message = "This field is required")
    private String description;
    @Positive(message = "this value can't be negative or zero")
    private BigDecimal price;
    private List<String> imgUrl;
    private String coverImg;

    public Product toProduct() {
        return Product.builder()
                .name(name)
                .skuCode(skuCode)
                .description(description)
                .price(price)
                .imgUrl(imgUrl)
                .coverImg(coverImg)
                .build();
    }
}
