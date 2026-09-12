package com.example.campus.service;

import com.example.campus.dto.RegisterRequest;
import com.example.campus.entity.User;
import com.example.campus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户业务逻辑层
 * 处理所有与用户相关的业务操作
 */
@Service
@RequiredArgsConstructor  // 自动生成包含所有final字段的构造函数（依赖注入）
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户注册
     *
     * @param request 注册请求，包含用户名、密码、邮箱等
     * @return 注册成功的用户
     * @throws RuntimeException 如果用户名已存在
     */
    @Transactional  // 事务注解：保证操作的原子性，要么全部成功，要么全部失败
    public User register(RegisterRequest request) {
        // 1. 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 检查邮箱是否已被使用
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被使用");
        }

        // 3. 构建用户对象
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))  // 加密密码
                .email(request.getEmail())
                .phone(request.getPhone())
                .creditScore(100)  // 初始信用分
                .build();

        // 4. 保存到数据库
        return userRepository.save(user);
    }

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户对象
     * @throws RuntimeException 如果用户不存在
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 根据ID查找用户
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 更新用户个人信息
     *
     * @param username 用户名
     * @param email 新邮箱
     * @param phone 新手机号
     */
    @Transactional
    public void updateProfile(String username, String email, String phone) {
        User user = findByUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        userRepository.save(user);
    }

    /**
     * 修改密码
     *
     * @param username 用户名
     * @param currentPassword 当前密码
     * @param newPassword 新密码
     * @throws RuntimeException 如果当前密码错误
     */
    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = findByUsername(username);

        // 验证当前密码是否正确
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("当前密码错误");
        }

        // 更新为新密码（加密后）
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}