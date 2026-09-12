package com.example.campus.service;

import com.example.campus.entity.User;
import com.example.campus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户详情服务
 * 实现Spring Security的UserDetailsService接口
 * 用于从数据库加载用户信息进行登录验证
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 根据用户名加载用户信息
     *
     * Spring Security会调用这个方法来获取用户信息：
     * 1. 用户提交用户名和密码
     * 2. Spring Security调用这个方法，传入用户名
     * 3. 从数据库查找用户
     * 4. 返回UserDetails对象（包含用户名、密码、权限等）
     * 5. Spring Security比对密码是否正确
     *
     * @param username 用户名
     * @return UserDetails对象
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 从数据库查找用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        // 2. 构建UserDetails对象
        // 第一个参数：用户名
        // 第二个参数：密码（已加密）
        // 第三个参数：权限列表（暂时为空，后面可以添加角色权限）
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}