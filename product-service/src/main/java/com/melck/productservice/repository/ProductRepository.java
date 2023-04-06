package com.melck.productservice.repository;

import com.melck.productservice.entity.Product;
import jakarta.persistence.SecondaryTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIdIn(Set<Long> productsId);

    Optional<Product> findBySkuCode(String skuCode);

//    @Query("SELECT DISTINCT obj FROM Product obj WHERE "
//            + "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%'))) ")
//    Page<Product> find(String name, Pageable pageable);


    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
    List<Product> findProductsWithCategories(List<Product> products);


}
