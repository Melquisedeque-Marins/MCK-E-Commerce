package com.melck.productservice.repository;

import com.melck.productservice.entity.Product;
import jakarta.persistence.SecondaryTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIdIn(Set<Long> productsId);

    Optional<Product> findBySkuCode(String skuCode);
}
