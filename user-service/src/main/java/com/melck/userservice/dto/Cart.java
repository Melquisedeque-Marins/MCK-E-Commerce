package com.melck.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    private Long id;
    private String cartNumber;
    private Set<Long> listOfProductsId = new HashSet<>();
}
