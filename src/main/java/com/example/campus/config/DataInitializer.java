package com.example.campus.config;

import com.example.campus.entity.Category;
import com.example.campus.entity.User;
import com.example.campus.repository.CategoryRepository;
import com.example.campus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 应用启动时初始化基础数据
 * 1. 预置商品分类
 * 2. 创建演示账号（admin / 123456）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initCategories();
        initDemoUser();
    }

    private void initCategories() {
        if (categoryRepository.count() > 0) {
            return;
        }
        List<Category> categories = Arrays.asList(
                Category.builder().name("教材书籍").icon("📚").sortOrder(1).description("课本、考研资料、课外书").build(),
                Category.builder().name("电子数码").icon("💻").sortOrder(2).description("手机、电脑、平板、配件").build(),
                Category.builder().name("生活用品").icon("🧴").sortOrder(3).description("日用品、小电器、收纳").build(),
                Category.builder().name("服饰鞋包").icon("👕").sortOrder(4).description("衣服、鞋子、包包").build(),
                Category.builder().name("运动户外").icon("🏀").sortOrder(5).description("球类、健身、户外装备").build(),
                Category.builder().name("自行车/代步").icon("🚲").sortOrder(6).description("自行车、电动车、滑板").build(),
                Category.builder().name("乐器文具").icon("🎸").sortOrder(7).description("乐器、文具、绘画工具").build(),
                Category.builder().name("其他闲置").icon("📦").sortOrder(8).description("各种杂物").build()
        );
        categoryRepository.saveAll(categories);
        log.info("已初始化 {} 个商品分类", categories.size());
    }

    private void initDemoUser() {
        if (userRepository.existsByUsername("admin")) {
            return;
        }
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .email("admin@campus.edu")
                .phone("13800000000")
                .creditScore(100)
                .build();
        userRepository.save(admin);
        log.info("已创建演示账号：admin / 123456");
    }
}