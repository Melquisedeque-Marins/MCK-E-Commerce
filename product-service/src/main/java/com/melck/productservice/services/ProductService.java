package com.melck.productservice.services;

import com.melck.productservice.dto.ProductRequest;
import com.melck.productservice.dto.ProductResponse;
import com.melck.productservice.entity.Product;
import com.melck.productservice.repository.ProductRepository;
import com.melck.productservice.services.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductResponse insert (ProductRequest productRequest) {
        var product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .rate(productRequest.getRate())
                .price(productRequest.getPrice())
                .imgUrl(productRequest.getImgUrl())
                .skuCode(productRequest.getSkuCode())
                .build();

        return new ProductResponse(repository.save(product));
    }

    public Product getProductById(Long id) {
        Optional<Product> product = repository.findById(id);
        return product.orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public List<Product> getAllProduct() {
        return repository.findAll();
    }


    public List<ProductResponse> getProductsInACart(Set<Long> productsId) {
        return repository.findByIdIn(productsId)
                .stream()
                .map(ProductResponse::new)
                .toList();
    }
}
