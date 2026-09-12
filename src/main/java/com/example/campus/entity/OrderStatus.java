package com.example.campus.entity;

/**
 * 订单状态枚举
 */
public enum OrderStatus {
    /** 待确认（买家已下单，等待卖家确认） */
    PENDING("待确认"),
    /** 交易中（卖家已确认） */
    TRADING("交易中"),
    /** 已完成 */
    COMPLETED("已完成"),
    /** 已取消 */
    CANCELLED("已取消");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}