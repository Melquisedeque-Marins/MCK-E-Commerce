package com.melck.cartservice.dto;

import com.melck.cartservice.entity.Cart;
import com.melck.cartservice.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    private Long id;
    private String cartNumber;
    private List<ProductDTO> listOfProducts = new ArrayList<>();

    public CartResponse(Cart cart) {


    }
}
