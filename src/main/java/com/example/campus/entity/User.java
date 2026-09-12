package com.example.campus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库中的 users 表
 *
 * @Data：自动生成getter/setter/toString等方法
 * @Builder：支持链式创建对象
 * @NoArgsConstructor：生成无参构造函数
 * @AllArgsConstructor：生成全参构造函数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")  // 指定对应的数据库表名
public class User {

    /**
     * 用户ID，主键，自增
     * 数据库会自动为每个用户分配一个唯一的ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名，唯一，不能为空
     * 用于登录和显示
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码，不能为空
     * 存储的是加密后的密码，不是明文
     */
    @Column(nullable = false)
    private String password;

    /**
     * 邮箱，可选
     */
    @Column(length = 100)
    private String email;

    /**
     * 手机号，可选
     */
    @Column(length = 20)
    private String phone;

    /**
     * 头像URL，可选
     */
    @Column(length = 255)
    private String avatar;

    /**
     * 信用评分，默认100分
     * 交易成功加分，违约扣分
     */
    @Builder.Default
    @Column(nullable = false)
    private Integer creditScore = 100;

    /**
     * 创建时间，自动生成
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间，自动更新
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}