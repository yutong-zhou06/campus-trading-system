package com.example.campus.config;

import com.example.campus.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 安全配置
 *
 * 这个配置决定了：
 * 1. 哪些页面需要登录才能访问
 * 2. 登录页面在哪里
 * 3. 登录成功后跳转到哪里
 * 4. 密码如何加密
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    /**
     * 密码编码器
     * BCrypt是一种安全的密码加密算法
     * 即使数据库泄露，密码也不容易被破解
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     * 用于处理用户登录验证
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 安全过滤器链配置
     * 定义哪些页面需要登录，哪些可以公开访问
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护（开发阶段简化，上线时建议开启）
            .csrf().disable()
            // 配置页面访问权限
            .authorizeRequests()
                // 公开页面：任何人可以访问
                .antMatchers("/", "/index", "/register", "/login", "/error",
                        "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .antMatchers(org.springframework.http.HttpMethod.GET,
                        "/products", "/products/*", "/users/*").permitAll()
                // 其他页面：需要登录才能访问
                .anyRequest().authenticated()
            .and()
            // 登录配置
            .formLogin()
                // 登录页面地址
                .loginPage("/login")
                // 登录表单提交地址
                .loginProcessingUrl("/login")
                // 登录成功的默认跳转地址
                .defaultSuccessUrl("/", true)
                // 登录失败的跳转地址
                .failureUrl("/login?error=true")
                // 允许所有用户访问登录页
                .permitAll()
            .and()
            // 登出配置
            .logout()
                // 登出地址
                .logoutUrl("/logout")
                // 登出成功后的跳转地址
                .logoutSuccessUrl("/")
                // 允许所有用户访问登出
                .permitAll()
            .and()
            // 使用自定义的UserDetailsService进行用户认证
            .userDetailsService(userDetailsService);

        return http.build();
    }
}