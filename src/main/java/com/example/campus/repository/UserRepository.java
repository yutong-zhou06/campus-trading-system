package com.example.campus.repository;

import com.example.campus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 * 继承JpaRepository，自动获得CRUD（增删改查）功能
 *
 * JpaRepository<User, Long> 的含义：
 * - User：操作的实体类型
 * - Long：主键ID的类型
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     * Spring Data JPA会自动根据方法名生成SQL：
     * SELECT * FROM users WHERE username = ?
     *
     * Optional：表示可能找到，也可能找不到
     * 比直接返回User更安全，避免NullPointerException
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否已存在
     * 返回true表示已存在，false表示不存在
     * 自动生成SQL：SELECT COUNT(*) FROM users WHERE username = ?
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     */
    boolean existsByEmail(String email);
}