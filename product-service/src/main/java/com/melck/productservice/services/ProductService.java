package com.melck.productservice.services;

import com.melck.productservice.dto.ProductRequest;
import com.melck.productservice.dto.ProductResponse;
import com.melck.productservice.dto.Review;
import com.melck.productservice.entity.Product;
import com.melck.productservice.repository.ProductRepository;
import com.melck.productservice.services.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Var;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;
    private final WebClient webClient;

    @Transactional
    public ProductResponse insert (ProductRequest productRequest) {
        var product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .imgUrl(productRequest.getImgUrl())
                .skuCode(productRequest.getSkuCode())
                .build();
        repository.save(product);
        var productResponse = new ProductResponse(product);
        productResponse.setQtyReviews(0);

        return productResponse;
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Optional<Product> productOptional = repository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Review[] reviews = webClient.get()
                .uri("http://localhost:8084/api/v1/reviews/" + product.getId())
                .retrieve()
                .bodyToMono(Review[].class)
                .block();

        List<Integer> ratings = Arrays.stream(reviews).map(review -> (review.getRate())).toList();

        double average = ratings.stream().mapToInt(r -> r).average().orElse(0);


        var productResponse = new ProductResponse(product);
        productResponse.setQtyReviews(reviews.length);
        productResponse.setRate(average);

        return productResponse;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProduct() {
        List<ProductResponse> products = repository.findAll()
                .stream()
                .map(this::mapProductToProductResponse)
                .toList();
        return products;
    }


    public List<ProductResponse> getProductsInACart(Set<Long> productsId) {
        return repository.findByIdIn(productsId)
                .stream()
                .map(ProductResponse::new)
                .toList();
    }

    private ProductResponse mapProductToProductResponse(Product product) {
        Review[] reviews = webClient.get()
                .uri("http://localhost:8084/api/v1/reviews/" + product.getId())
                .retrieve()
                .bodyToMono(Review[].class)
                .block();

        List<Integer> ratings = Arrays.stream(reviews).map(review -> (review.getRate())).toList();

        double average = ratings.stream().mapToInt(r -> r).average().orElse(0);

        var productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .skuCode(product.getSkuCode())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .rate(product.getRate())
                .build();
        productResponse.setQtyReviews(reviews.length);
        productResponse.setRate(average);

        return productResponse;
    }
}
