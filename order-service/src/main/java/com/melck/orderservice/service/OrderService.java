package com.melck.orderservice.service;

import com.melck.orderservice.dto.OrderItemDTO;
import com.melck.orderservice.dto.OrderRequest;
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
    public Order placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setCartId(2L);

        List<OrderItem> orderItemList = orderRequest.getOrderItemDTOList().stream()
                .map(this::mapOrderItemDtoToOrderItem)
                .toList();

        List<OrderItem> orderItemList1 = orderItemList.stream().map(orderItem -> {
            double total = orderItem.getQuantity() * orderItem.getPrice();
            orderItem.setAmountPerItem(total);
            return orderItem;
        }).toList();

        List<Double> totalPer = orderItemList.stream().map(OrderItem::getAmountPerItem).toList();


        order.setOrderItemList(orderItemList);
        order.setAmount(totalPer.stream().mapToDouble(d -> d).sum());
        
//        OrderItem[] orderItemsList = webClient.get()
//                .uri("localhost:8080/api/v1/cart/" + 1)
//                .retrieve()
//                .bodyToMono(OrderItem[].class)
//                .block();


        return orderRepository.save(order);
    }

    private OrderItem mapOrderItemDtoToOrderItem (OrderItemDTO dto) {
        return OrderItem.builder()
                .skuCode(dto.getSkuCode())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();
    }


}
