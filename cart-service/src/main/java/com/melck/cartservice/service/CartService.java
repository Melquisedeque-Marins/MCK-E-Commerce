package com.melck.cartservice.service;

import com.melck.cartservice.dto.CartRequest;
import com.melck.cartservice.dto.CartResponse;
import com.melck.cartservice.dto.ProductDTO;
import com.melck.cartservice.entity.Cart;
import com.melck.cartservice.entity.Product;
import com.melck.cartservice.repository.CartRepository;
import com.melck.cartservice.service.exception.CartNotFoundException;
import com.melck.cartservice.service.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository repository;
    private final WebClient webClient;

    private final RestTemplate restTemplate;


    public Cart newCart() {
        Cart cart = new Cart();
        cart.setCartNumber(UUID.randomUUID().toString());
        return repository.save(cart);

    } public Cart addProductToCart(Long cartId, CartRequest cartRequest) {
        Cart cart = repository.getReferenceById(cartId);

        Product product = webClient.get()
                .uri("http://localhost:8080/api/v1/products/" + cartRequest.getProductId())
                .retrieve()
                .bodyToMono(Product.class)
                .block();

        try {
            cart.getListOfProductsId().add(product.getId());
            return repository.save(cart);
        } catch (WebClientResponseException e) {
            throw new ProductNotFoundException("Product not found", e);
        }
    }

    public Cart removeProductToCart(Long cartId, CartRequest cartRequest) {
        Cart cart = repository.getReferenceById(cartId);

        Mono<Product> product = webClient.get()
                .uri("http://localhost:8080/api/v1/products/" + cartRequest.getProductId())
                .retrieve()
                .bodyToMono(Product.class);

        try {
            cart.getListOfProductsId().remove(cartRequest.getProductId());
            return repository.save(cart);
        } catch (WebClientResponseException e) {
            throw new ProductNotFoundException("Product not found", e);
        }
    }

    public CartResponse getCartById(Long cartId) {
        Optional<Cart> cartOptional = repository.findById(cartId);
        Cart cart = cartOptional.orElseThrow(() -> new CartNotFoundException("Cart not found"));

        Set<Long> productsId = cart.getListOfProductsId();

        Product[] products = webClient.get()
                .uri("http://localhost:8080/api/v1/products/cart",
                        uriBuilder -> uriBuilder.queryParam("productsId", productsId ).build())
                .retrieve()
                .bodyToMono(Product[].class)
                .block();

        List<ProductDTO> productsDTO = Arrays
                .stream(products)
                .map(this::mapProductToProductDTO)
                .toList();

        CartResponse response = CartResponse.builder()
                .cartNumber(cart.getCartNumber())
                .id(cart.getId())
                .listOfProducts(productsDTO)
                .build();

        return response;
    }

    private Product mapDTOtoProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSkuCode(dto.getSkuCode());
        return product;
    }

    private ProductDTO mapProductToProductDTO(Product product) {
        ProductDTO productDTO = ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .skuCode(product.getSkuCode())
                .description(product.getDescription())
                .imgUrl(product.getImgUrl())
                .build();
        return productDTO;
    }

}