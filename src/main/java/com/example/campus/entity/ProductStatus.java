package com.example.campus.entity;

/**
 * 商品状态枚举
 */
public enum ProductStatus {
    /** 在售 */
    AVAILABLE("在售"),
    /** 已售出 */
    SOLD("已售出"),
    /** 已下架 */
    REMOVED("已下架");

    private final String label;

    ProductStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}