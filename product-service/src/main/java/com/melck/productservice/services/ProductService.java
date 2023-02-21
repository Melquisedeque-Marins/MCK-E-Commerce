package com.melck.productservice.services;

import com.melck.productservice.client.ReviewClient;
import com.melck.productservice.config.ModelMapperConfig;
import com.melck.productservice.dto.ProductRequest;
import com.melck.productservice.dto.ProductResponse;
import com.melck.productservice.dto.Review;
import com.melck.productservice.entity.Product;
import com.melck.productservice.repository.ProductRepository;
import com.melck.productservice.services.exceptions.ProductNotFoundException;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;
    private final ReviewClient reviewClient;
    private final ModelMapper modelMapper;

    @Transactional
    public ProductResponse insert (ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        product.setQtyReviews(0);
        product.setRate(0.0);
        log.info("Saving the new product");
        return new ProductResponse(repository.save(product));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "product", key = "#id")
    public ProductResponse getProductById(Long id) {
        log.info("Searching product with id {} ", id);
        Product product = repository.findById(id).orElseThrow(() -> {
            log.error("Product with id: " + id + " not found");
            return new ProductNotFoundException("Product with id: " + id + " not found");
        });
        log.info("returning product with id: {} ", id);
        return new ProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products")
    public List<ProductResponse> getAllProduct() {
        log.info("Searching for products...");
        return repository.findAll()
                .stream()
                .map(ProductResponse::new)
                .toList();
    }

    @Transactional
    public ProductResponse editProduct(Long id, ProductRequest productRequest) {
        Product product = repository.getReferenceById(id);
        if (productRequest.getName() != product.getName()){
            product.setName(productRequest.getName());
        }
        if (productRequest.getPrice() != product.getPrice()){
            product.setPrice(productRequest.getPrice());
        }
        if (!productRequest.getSkuCode().equals(product.getSkuCode())){
            product.setSkuCode(productRequest.getSkuCode());
        }
        return new ProductResponse(repository.save(product));
    }

    @Transactional
    @CacheEvict(value = "product", allEntries = true)
    @RabbitListener(queues = "reviews.v1.review-created")
    public void updateRate(ProductResponse product) {
        log.info("Updating product with id: {} , received from review service event", product.getId());
        Product existingProduct = repository.findById(product.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found id: " + product.getId()));
        existingProduct.setRate(product.getRate());
        existingProduct.setQtyReviews(product.getQtyReviews());
        repository.save(existingProduct);
    }

    @Transactional
    public List<ProductResponse> getProductsInACart(Set<Long> productsId) {
        return repository.findByIdIn(productsId)
                .stream()
                .map(ProductResponse::new)
                .toList();
    }

    private ProductResponse getProductWithReviews(Product product) {
//        Review[] reviews = reviewClient.getReviewByProductId(product.getId());
//        List<Integer> ratings = Arrays.stream(reviews).map(Review::getRate).toList();
//        double average = ratings.stream().mapToInt(r -> r).average().orElse(0);

        var productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .skuCode(product.getSkuCode())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .build();
//        productResponse.setQtyReviews(reviews.length);
//        productResponse.setRate(average);

        return productResponse;
    }
}
