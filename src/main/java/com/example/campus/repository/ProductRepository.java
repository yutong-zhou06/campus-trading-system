package com.example.campus.repository;

import com.example.campus.entity.Category;
import com.example.campus.entity.Product;
import com.example.campus.entity.ProductStatus;
import com.example.campus.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByStatusOrderByCreatedAtDesc(ProductStatus status, Pageable pageable);

    Page<Product> findByStatusAndCategoryOrderByCreatedAtDesc(ProductStatus status, Category category, Pageable pageable);

    Page<Product> findByStatusAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
            ProductStatus status, String keyword, Pageable pageable);

    java.util.List<Product> findBySellerOrderByCreatedAtDesc(User seller);

    List<Product> findTop8ByStatusOrderByCreatedAtDesc(ProductStatus status);

    long countBySeller(User seller);

    long countByStatus(ProductStatus status);

    /**
     * 综合搜索：关键词 + 分类（分类为空时忽略分类条件）
     */
    @Query("SELECT p FROM Product p WHERE p.status = :status " +
           "AND (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
           "ORDER BY p.createdAt DESC")
    Page<Product> search(@Param("status") ProductStatus status,
                         @Param("keyword") String keyword,
                         @Param("categoryId") Long categoryId,
                         Pageable pageable);
}