package com.melck.inventoryservice.service;

import com.melck.inventoryservice.dto.InventoryRequest;
import com.melck.inventoryservice.dto.InventoryResponse;
import com.melck.inventoryservice.entity.Inventory;
import com.melck.inventoryservice.repository.InventoryRepository;
import com.melck.inventoryservice.service.exceptions.ResourceNotFoundException;
import com.melck.inventoryservice.service.exceptions.SkuCodeAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ResourceClosedException;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @RabbitListener(queues = "products.v1.product-created")
    public Inventory registerInInventory(String skuCode) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findBySkuCode(skuCode);

        if (inventoryOptional.isPresent()) {
            throw new SkuCodeAlreadyRegisteredException("product with sku code: " + skuCode + " is already registered!");
        }
        Inventory inventory = new Inventory();
        inventory.setQuantity(0);
        inventory.setSkuCode(skuCode);
        return inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
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

    @Transactional
    public InventoryResponse updateQuantityInInventory(String skuCode, InventoryRequest inventoryRequest) {
        Inventory actualInventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new ResourceNotFoundException("register with SKu Code: " + skuCode + " not found"));
        actualInventory.setQuantity(inventoryRequest.getQuantity());

        return InventoryResponse.of(inventoryRepository.save(actualInventory));
    }
}
