package com.melck.inventoryservice.dto;

import com.melck.inventoryservice.enuns.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {

    private String skuCode;
    private Integer quantity;
//    private ItemStatus status;
    private Boolean isInStock;
}
