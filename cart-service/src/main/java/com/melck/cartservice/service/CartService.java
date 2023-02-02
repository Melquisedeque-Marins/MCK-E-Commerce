package com.melck.cartservice.service;

import com.melck.cartservice.dto.CartRequest;
import com.melck.cartservice.dto.ProductDTO;
import com.melck.cartservice.entity.Cart;
import com.melck.cartservice.entity.Product;
import com.melck.cartservice.repository.CartRepository;
import com.melck.cartservice.service.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository repository;
    private final WebClient webClient;

    private final RestTemplate restTemplate;

//    public Cart addToCart(CartRequest cartRequest) {
//        Cart cart = new Cart();
//        cart.setCartNumber(UUID.randomUUID().toString());
//
//        List<Product> products = cartRequest.getListOfProductsDTO()
//                .stream()
//                .map(this::mapDTOtoProduct)
//                .toList();
//        cart.setListOfProducts(products);
//
//        List<String> skuCodes = cart.getListOfProducts()
//                .stream()
//                .map(Product::getSkuCode)
//                .toList();
//
//    return repository.save(cart);
//    }

    public Cart addToCart(CartRequest cartRequest) {
        Cart cart = new Cart();
        cart.setCartNumber(UUID.randomUUID().toString());

        Mono<Product> product = webClient.get()
                .uri("http://localhost:8080/api/v1/products/" + cartRequest.getProductId())
                .retrieve()
                .bodyToMono(Product.class);

        try {
            cart.getListOfProductId().add( product.block().getId());
            return repository.save(cart);
        } catch (WebClientResponseException e) {
            throw new ProductNotFoundException("Product not found", e);
        }
    }


    private Product mapDTOtoProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setSkuCode(dto.getSkuCode());
        return product;
    }
}
