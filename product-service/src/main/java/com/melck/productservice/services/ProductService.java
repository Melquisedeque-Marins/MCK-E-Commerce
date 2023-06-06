package com.melck.productservice.services;

import com.melck.productservice.dto.ProductRequest;
import com.melck.productservice.dto.ProductResponse;
import com.melck.productservice.entity.Category;
import com.melck.productservice.entity.Product;
import com.melck.productservice.repository.CategoryRepository;
import com.melck.productservice.repository.ProductRepository;
import com.melck.productservice.services.exceptions.ProductNotFoundException;
import com.melck.productservice.services.exceptions.SkuCodeIsAlreadyInUse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public ProductResponse registerProduct (ProductRequest productRequest) {
        Optional<Product> productOpt = repository.findBySkuCode(productRequest.getSkuCode());

        if (productOpt.isPresent()) {
            throw new SkuCodeIsAlreadyInUse("The SKU code: " + productRequest.getSkuCode() + " is already in use ");
        }
        Product product = productRequest.toProduct();
        product.setQtyReviews(0);
        product.setRate(0.0);
        product.setIsInSale(false);
        product.setPromotionalPrice(BigDecimal.valueOf(0));
        log.info("Saving the new product");
        String routingKey = "products.v1.product-created";
        rabbitTemplate.convertAndSend(routingKey, product.getSkuCode());
        return ProductResponse.of(repository.save(product));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "product", key = "#id")
    public ProductResponse getProductById(Long id) {
        log.info("Searching product with id {} ", id);
        Product product = findOrError(id);
        return ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products")
    public Page<ProductResponse> getAllProduct(Long categoryId, String name, Boolean isInSale, Pageable pageable) {
        List<Category> categories = (categoryId == 0) ? null : List.of(categoryRepository.getReferenceById(categoryId));
        Page<Product> page = repository.find(categories, name, isInSale, pageable);
        repository.findProductsWithCategories(page.stream().collect(Collectors.toList()));
        return page.map(ProductResponse::of);
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "products")
    public Page<ProductResponse> getAllProductPerCategory(Long categoryId, Pageable pageable) {
        List<Category> categories = (categoryId == 0) ? null : List.of(categoryRepository.getReferenceById(categoryId));
        Page<Product> page = repository.findPerCategory(categories, pageable);
        repository.findProductsWithCategories(page.stream().collect(Collectors.toList()));
        return page.map(ProductResponse::of);
    }

    @Transactional
    @CacheEvict(value = "product", allEntries = true)
    public ProductResponse editProduct(Long id, ProductRequest productRequest) {
        Product product = findOrError(id);
        if (productRequest.getName().equals(product.getName())){
            product.setName(productRequest.getName());
        }
        if (productRequest.getPrice().equals(product.getPrice())){
            product.setPrice(productRequest.getPrice());
        }
        if (!productRequest.getSkuCode().equals(product.getSkuCode())){
            product.setSkuCode(productRequest.getSkuCode());
        }
        return ProductResponse.of(repository.save(product));
    }

    @Transactional
    @CacheEvict(value = "product", allEntries = true)
    @RabbitListener(queues = "reviews.v1.review-created")
    public void updateRate(ProductResponse product) {
        log.info("Updating product with id: {} , received from review service event", product.getId());
        Product existingProduct = findOrError(product.getId());
        existingProduct.setRate(product.getRate());
        existingProduct.setQtyReviews(product.getQtyReviews());
        repository.save(existingProduct);
    }

    @Transactional
    public List<ProductResponse> getProductsInACart(Set<Long> productsId) {
        return repository.findByIdIn(productsId)
                .stream()
                .map(ProductResponse::of)
                .toList();
    }

    private Product findOrError(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Product with id: " + id + " not found");
            return new ProductNotFoundException("Product with id: " + id + " not found");
        });
    }

  }
