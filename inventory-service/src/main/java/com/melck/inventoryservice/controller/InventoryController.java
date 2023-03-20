package com.melck.inventoryservice.controller;

import com.melck.inventoryservice.dto.InventoryRequest;
import com.melck.inventoryservice.dto.InventoryResponse;
import com.melck.inventoryservice.entity.Inventory;
import com.melck.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @PutMapping("/skuCode")
    public ResponseEntity<InventoryResponse> updateQuantityInInventory (@PathVariable String skuCode, @RequestBody InventoryRequest inventoryRequest) {
        InventoryResponse inventory = inventoryService.updateQuantityInInventory(skuCode, inventoryRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(inventory.getId()).toUri();
        return ResponseEntity.created(uri).body(inventory);
    }

    @GetMapping("/check-stock")
    public ResponseEntity<List<InventoryResponse>> isInStock (@RequestParam List<String> skuCode) {
        return ResponseEntity.ok(inventoryService.isInStock(skuCode));
    }

    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryResponse> isInStockBySkuCode (@PathVariable String skuCode) {
        return ResponseEntity.ok(inventoryService.isInStockBySkuCode(skuCode));
    }



}
