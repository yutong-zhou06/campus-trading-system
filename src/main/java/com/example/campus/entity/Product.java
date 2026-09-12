package com.example.campus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 对应数据库中的 products 表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 商品标题 */
    @Column(nullable = false, length = 100)
    private String title;

    /** 商品描述 */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** 售价 */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** 原价（可选，用于显示折扣） */
    @Column(precision = 10, scale = 2)
    private BigDecimal originalPrice;

    /** 商品图片URL */
    @Column(length = 255)
    private String imageUrl;

    /** 新旧程度：如 全新、9成新、8成新 */
    @Column(length = 20)
    private String conditionLevel;

    /** 交易地点 */
    @Column(length = 100)
    private String location;

    /** 浏览次数 */
    @Builder.Default
    private Integer viewCount = 0;

    /** 商品状态 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ProductStatus status = ProductStatus.AVAILABLE;

    /** 所属分类 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /** 发布者（卖家） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}