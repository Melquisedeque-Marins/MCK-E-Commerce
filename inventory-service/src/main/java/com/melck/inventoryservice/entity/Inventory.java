package com.melck.inventoryservice.entity;

import com.melck.inventoryservice.dto.InventoryResponse;
import com.melck.inventoryservice.enuns.ItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
//    @Enumerated(EnumType.STRING)
//    private ItemStatus status;
    private Integer quantity;



}
