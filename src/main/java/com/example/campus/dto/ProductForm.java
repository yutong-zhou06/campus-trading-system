package com.example.campus.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * 商品发布/编辑表单
 */
@Data
public class ProductForm {

    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Long categoryId;
    private String conditionLevel;
    private String location;
    private MultipartFile image;
}