package com.melck.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {

    private String skuCode;
    private Integer quantity;
//    private ItemStatus status;
    private Boolean isInStock;
}
