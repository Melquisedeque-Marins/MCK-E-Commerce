package com.melck.productservice.repository;

import com.melck.productservice.entity.Category;
import com.melck.productservice.entity.Product;
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

    @Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE "
            + "(COALESCE(:categories) IS NULL OR cats IN :categories) AND "
            + "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%'))) AND "
            + "(obj.isInSale = :isInSale)" )
    Page<Product> find(List<Category> categories, String name, Boolean isInSale, Pageable pageable);

    @Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE "
            + "(COALESCE(:categories) IS NULL OR cats IN :categories)")
    Page<Product> findPerCategory(List<Category> categories, Pageable pageable);


    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
    List<Product> findProductsWithCategories(List<Product> products);

    @Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE "
            + "(COALESCE(:categories) IS NULL OR cats IN :categories) AND "
            + "(obj.price <= :maxPrice) AND (obj.price >= :minPrice) " )
    Page<Product> filter(List<Category> categories, Long minPrice, Long maxPrice, Pageable pageable);


}
