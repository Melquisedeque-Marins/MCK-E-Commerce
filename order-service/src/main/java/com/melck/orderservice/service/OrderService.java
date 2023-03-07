package com.melck.orderservice.service;

import com.melck.orderservice.dto.*;
import com.melck.orderservice.entity.Order;
import com.melck.orderservice.entity.OrderItem;
import com.melck.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Transactional
    public Order placeOrder(Long cartId) {
        Order order = new Order();

        Cart cart = webClient.get()
                .uri("localhost:8080/api/v1/cart/" + cartId)
                .retrieve()
                .bodyToMono(Cart.class)
                .block();

        if(cart==null) {
            return null;
        }

        List<OrderItem> orderItemList = cart.getListOfProducts().stream()
                .map(this::mapProductToOrderItem)
                .toList();

        List<String> skuList = orderItemList.stream()
                .map(OrderItem::getSkuCode)
                .toList();

        InventoryResponse[] inventoryResponsesArray = webClient.get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuList).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        assert inventoryResponsesArray != null;
        boolean allProductsInStock =  Arrays.stream(inventoryResponsesArray)
                .allMatch(InventoryResponse::getIsInStock);

        if (allProductsInStock) {
            List<Double> totalPerItem = orderItemList.stream().map(OrderItem::getAmountPerItem).toList();
            List<Integer> totalItem = orderItemList.stream().map(OrderItem::getQuantity).toList();
            order.setCartId(cartId);
            order.setOrderItemList(orderItemList);
            order.setProductQuantity(totalItem.stream().mapToInt(d -> d).sum());
            order.setAmount(totalPerItem.stream().mapToDouble(d -> d).sum());

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
