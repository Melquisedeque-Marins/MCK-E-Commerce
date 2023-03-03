package com.melck.cartservice.service;

import com.melck.cartservice.client.ProductClient;
import com.melck.cartservice.dto.CartRequest;
import com.melck.cartservice.dto.CartResponse;
import com.melck.cartservice.dto.ProductDTO;
import com.melck.cartservice.entity.Cart;
import com.melck.cartservice.entity.CartItem;
import com.melck.cartservice.dto.Product;
import com.melck.cartservice.repository.CartItemRepository;
import com.melck.cartservice.repository.CartRepository;
import com.melck.cartservice.service.exception.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository repository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;
    private final ModelMapper modelMapper;

    @Transactional
    public Cart newCart() {
        Cart cart = new Cart();
        cart.setCartNumber(UUID.randomUUID().toString());
        return repository.save(cart);
    }

    @Transactional
    public Cart addProductToCart(Long cartId, CartRequest cartRequest) {
        Cart cart = repository.getReferenceById(cartId);
        if (cartRequest.getQuantity() == null) {
            cartRequest.setQuantity(1);
        }
        Product product = productClient.getProductInProductService(cartRequest.getProductId());

        CartItem cartItem = cart.getListOfCartItems().stream()
                .filter(c -> Objects.equals(cartRequest.getProductId(), c.getProductId()))
                .findAny()
                .orElse(null);

        if(cartItem!=null ) {
            if (cartRequest.getQuantity()<=0) {
                cart.getListOfCartItems().remove(cartItem);
                cartItemRepository.delete(cartItem);
                return repository.save(cart);
            }
            cartItem.setQuantity(cartRequest.getQuantity());
            cartItemRepository.save(cartItem);
            return repository.save(cart);
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setProductId(cartRequest.getProductId());


        if (cartRequest.getQuantity()!=1){
            newCartItem.setQuantity(cartRequest.getQuantity());
        }
        newCartItem.setQuantity(1);
        cart.getListOfCartItems().add(newCartItem);
        return repository.save(cart);
    }

    @Transactional
    public Cart removeProductToCart(Long cartId, CartRequest cartRequest) {
        Cart cart = repository.getReferenceById(cartId);
        Product product = productClient.getProductInProductService(cartRequest.getProductId());
        cart.getListOfCartItems().stream()
                .filter(c -> Objects.equals(cartRequest.getProductId(), c.getProductId()))
                .findAny()
                .ifPresent(cartItem -> {
                    cart.getListOfCartItems().remove(cartItem);
                    cartItemRepository.delete(cartItem);
                });
        return repository.save(cart);
    }

    @Transactional
    public Cart emptyTheCart(Long cartId) {
        Optional<Cart> cartOptional = repository.findById(cartId);
        Cart cart = cartOptional.orElseThrow(() -> new CartNotFoundException("Cart not found"));
        List<Long> cartItemsIds = cart.getListOfCartItems().stream().map(CartItem::getId).toList();
        cart.getListOfCartItems().clear();
        cartItemRepository.deleteAllById(cartItemsIds);
        return repository.save(cart);
    }

    @Transactional(readOnly = true)
    public CartResponse getCartById(Long cartId) {
        Optional<Cart> cartOptional = repository.findById(cartId);
        Cart cart = cartOptional.orElseThrow(() -> new CartNotFoundException("Cart not found"));
        Set<Long> productsId = cart.getListOfCartItems().stream().map(CartItem::getProductId).collect(Collectors.toSet());

        Product[] products = productClient.getProductInACartProductService(productsId);

        List<ProductDTO> productsDTO = Arrays
                .stream(products)
                .map(product -> mapProductToProductDTO(product, cart.getListOfCartItems()))
                .toList();

        return CartResponse.builder()
                .cartNumber(cart.getCartNumber())
                .id(cart.getId())
                .listOfProducts(productsDTO)
                .build();
    }

    private ProductDTO mapProductToProductDTO(Product product, Set<CartItem> items) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        items.stream().filter(cartItem -> productDTO.getId().equals(cartItem.getProductId())).findAny().ifPresent(item -> productDTO.setQuantity(item.getQuantity()));
        return productDTO;
    }
}