package com.melck.orderservice.service;

import com.melck.orderservice.dto.*;
import com.melck.orderservice.entity.Order;
import com.melck.orderservice.entity.OrderItem;
import com.melck.orderservice.repository.OrderRepository;
import com.melck.orderservice.service.exception.ProductIsOutOfStockException;
import com.melck.orderservice.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    private final RabbitTemplate rabbitTemplate;
    private final static String EXCHANGE = "orders.v1.order-placed";
    @Transactional
    public Order placeOrder(Long cartId) {
        try {
            log.info("Searching cart in the cart service");
            Cart cart = webClient.get()
                    .uri("http://localhost:8080/api/v1/cart/" + cartId)
                    .retrieve()
                    .bodyToMono(Cart.class)
                    .block();
            if(cart==null) {
                throw new ResourceNotFoundException("Cart with id: " + cartId + " not found");
            }
            log.info("converting products in order items ");
            List<OrderItem> orderItemList = cart.getListOfProducts().stream()
                    .map(this::mapProductToOrderItem)
                    .toList();
            log.info("collecting sku codes ");
            List<String> skuCodes = orderItemList.stream()
                    .map(OrderItem::getSkuCode)
                    .toList();

            log.info("Checking for the following {} products in the inventory service", skuCodes);
            InventoryResponse[] inventoryResponsesArray = webClient.get()
                    .uri("http://localhost:8080/api/v1/inventory/check-stock",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            log.info("check if {} is in stock ", (Object) inventoryResponsesArray);
            if(inventoryResponsesArray == null ){
                throw new ResourceNotFoundException("Products with ids: " + skuCodes + " not founded");
            }
            boolean allProductsInStock =  Arrays.stream(inventoryResponsesArray)
                    .allMatch(InventoryResponse::getIsInStock);

            if (allProductsInStock) {
                List<Double> amountsPerItem = orderItemList.stream().map(OrderItem::getAmountPerItem).toList();
                List<Integer> quantityPerItem = orderItemList.stream().map(OrderItem::getQuantity).toList();
                Order order = new Order();
                order.setCartId(cartId);
                order.setOrderItemList(orderItemList);
                order.setProductQuantity(quantityPerItem.stream().mapToInt(d -> d).sum());
                order.setAmount(amountsPerItem.stream().mapToDouble(d -> d).sum());
                var newOrder = orderRepository.save(order);
                log.info("order saved in database");
                rabbitTemplate.convertAndSend(EXCHANGE, "", order);
                log.info("Send message to orders.v1.order-placed exchange");
                return newOrder;
            }
            else {
                log.error("One or more products are currently out of stock");
                throw new ProductIsOutOfStockException("Product is not in stock, try again later");
            }
        } catch (WebClientResponseException e) {
            throw e;
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
