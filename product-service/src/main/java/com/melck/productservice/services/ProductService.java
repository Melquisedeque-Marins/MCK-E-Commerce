package com.melck.productservice.services;

import com.melck.productservice.dto.ProductRequest;
import com.melck.productservice.dto.ProductResponse;
import com.melck.productservice.entity.Product;
import com.melck.productservice.repository.ProductRepository;
import com.melck.productservice.services.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;

    @Transactional
    public ProductResponse insert (ProductRequest productRequest) {
        var product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .imgUrl(productRequest.getImgUrl())
                .skuCode(productRequest.getSkuCode())
                .build();

        return new ProductResponse(repository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Optional<Product> productOptional = repository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return new ProductResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProduct() {
        List<ProductResponse> products = repository.findAll()
                .stream()
                .map(product -> new ProductResponse(product))
                .toList();
        return products;
    }


    public List<ProductResponse> getProductsInACart(Set<Long> productsId) {
        return repository.findByIdIn(productsId)
                .stream()
                .map(ProductResponse::new)
                .toList();
    }
}
