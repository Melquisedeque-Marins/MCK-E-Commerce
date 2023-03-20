package com.melck.orderservice.service;

import com.melck.orderservice.dto.*;
import com.melck.orderservice.entity.Order;
import com.melck.orderservice.entity.OrderItem;
import com.melck.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Transactional
    public Order placeOrder(Long cartId) {
        Order order = new Order();
        Cart cart = webClient.get()
                .uri("http://localhost:8080/api/v1/cart/" + cartId)
                .retrieve()
                .bodyToMono(Cart.class)
                .block();

        if(cart==null) {
            return null;
        }
        log.info("converting products in order items ");
        List<OrderItem> orderItemList = cart.getListOfProducts().stream()
                .map(this::mapProductToOrderItem)
                .toList();
        log.info("collecting sku codes ");
        List<String> skuCodes = orderItemList.stream()
                .map(OrderItem::getSkuCode)
                .toList();

        log.info("check inventory for {}", skuCodes);
        InventoryResponse[] inventoryResponsesArray = webClient.get()
                .uri("http://localhost:8080/api/v1/inventory/check-stock",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        log.info("check if is in stock {}", (Object) inventoryResponsesArray);
        if(inventoryResponsesArray == null ){
            return null;
        }
        boolean allProductsInStock =  Arrays.stream(inventoryResponsesArray)
                .allMatch(InventoryResponse::getIsInStock);

        if (allProductsInStock) {
            List<Double> amountsPerItem = orderItemList.stream().map(OrderItem::getAmountPerItem).toList();
            List<Integer> quantityPerItem = orderItemList.stream().map(OrderItem::getQuantity).toList();
            order.setCartId(cartId);
            order.setOrderItemList(orderItemList);
            order.setProductQuantity(quantityPerItem.stream().mapToInt(d -> d).sum());
            order.setAmount(amountsPerItem.stream().mapToDouble(d -> d).sum());
            return orderRepository.save(order);
        }
        else {
            throw new IllegalArgumentException("Product is not in stock, try again later");
        }


    }

    private OrderItem mapProductToOrderItem(Product product) {
        return OrderItem.builder()
                .productId(product.getId())
                .skuCode(product.getSkuCode())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .amountPerItem(product.getPrice()* product.getQuantity())
                .build();
    }

}
