package com.melck.inventoryservice.service;

import com.melck.inventoryservice.dto.InventoryRequest;
import com.melck.inventoryservice.dto.InventoryResponse;
import com.melck.inventoryservice.entity.Inventory;
import com.melck.inventoryservice.enuns.ItemStatus;
import com.melck.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.ResourceClosedException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Inventory registerInInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = modelMapper.map(inventoryRequest, Inventory.class);
//        if (inventoryRequest.getQuantity()<0) {
//            inventory.setStatus(ItemStatus.OUT_OF_STOCK);
//        }
//        inventory.setStatus(ItemStatus.IN_STOCK);
        return inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .quantity(inventory.getQuantity())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }

    public InventoryResponse isInStockBySkuCode(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new ResourceClosedException("Inventory not found to sku code: " + skuCode));
        InventoryResponse response = modelMapper.map(inventory, InventoryResponse.class);
        response.setIsInStock(inventory.getQuantity() > 0);
        return response;
    }
}
