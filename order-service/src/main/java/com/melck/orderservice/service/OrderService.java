package com.melck.orderservice.service;

import com.melck.orderservice.dto.Cart;
import com.melck.orderservice.dto.OrderItemDTO;
import com.melck.orderservice.dto.OrderRequest;
import com.melck.orderservice.dto.Product;
import com.melck.orderservice.entity.Order;
import com.melck.orderservice.entity.OrderItem;
import com.melck.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

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

        List<OrderItem> orderItemList = cart.getListOfProducts().stream()
                .map(this::mapProductToOrderItem)
                .toList();

        List<Double> totalPerItem = orderItemList.stream().map(OrderItem::getAmountPerItem).toList();
        List<Integer> totalItem = orderItemList.stream().map(OrderItem::getQuantity).toList();

        order.setOrderItemList(orderItemList);
        order.setProductQuantity(totalItem.stream().mapToInt(d -> d).sum());
        order.setAmount(totalPerItem.stream().mapToDouble(d -> d).sum());

        return orderRepository.save(order);
    }

    private OrderItem mapProductToOrderItem(Product product) {
        OrderItem orderItem = OrderItem.builder()
                .productId(product.getId())
                .skuCode(product.getSkuCode())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .amountPerItem(product.getPrice()* product.getQuantity())
                .build();
        return orderItem;
    }

}
