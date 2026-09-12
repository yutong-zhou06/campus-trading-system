# 校园二手交易系统 - 从零开始开发指南

## 项目概述

这是一个完整的校园二手物品交易平台，使用现代Java Web技术栈构建。

## 技术栈详解

### 后端
- **Spring Boot 2.7.x**: 快速构建Spring应用的框架
- **Spring Security**: 安全框架，处理用户认证和授权
- **Spring Data JPA**: 数据访问层，简化数据库操作
- **MySQL**: 关系型数据库，存储业务数据

### 前端
- **Thymeleaf**: 服务器端模板引擎，动态生成HTML
- **Bootstrap 5**: 响应式UI框架，快速构建美观界面
- **JavaScript**: 前端交互逻辑
- **AJAX**: 异步数据请求

## 开发阶段详解

### 阶段1：项目初始化（预计1-2天）

#### 目标
创建一个可以运行的Spring Boot项目骨架

#### 任务清单
1. **环境准备**
   - 安装JDK 11+（你已有JDK 22）
   - 安装Maven 3.6+
   - 安装MySQL 8.0
   - 选择并安装IDE

2. **创建项目**
   - 使用Spring Initializr生成项目
   - 配置pom.xml依赖
   - 创建包结构

3. **数据库配置**
   - 配置application.yml
   - 测试数据库连接
   - 创建基础实体类

4. **版本控制**
   - 初始化Git仓库
   - 创建.gitignore文件
   - 首次提交

#### 交付物
- 可运行的Spring Boot项目
- 数据库连接配置
- Git仓库初始化

### 阶段2：用户模块（预计2-3天）

#### 目标
实现完整的用户系统，包括注册、登录、权限控制

#### 任务清单
1. **数据库设计**
   ```sql
   CREATE TABLE users (
       id BIGINT PRIMARY KEY AUTO_INCREMENT,
       username VARCHAR(50) UNIQUE NOT NULL,
       password VARCHAR(100) NOT NULL,
       email VARCHAR(100),
       phone VARCHAR(20),
       avatar VARCHAR(255),
       credit_score INT DEFAULT 100,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   );
   ```

2. **实体类创建**
   - User实体类
   - UserRepository接口
   - UserService业务层
   - UserController控制器

3. **安全配置**
   - Spring Security配置
   - 密码加密（BCrypt）
   - 登录/注销处理
   - 权限控制

4. **前端页面**
   - 注册页面
   - 登录页面
   - 个人中心页面
   - 导航栏组件

#### 交付物
- 用户注册/登录功能
- 会话管理
- 基础权限控制

### 阶段3：商品模块（预计3-4天）

#### 目标
实现商品的完整生命周期管理

#### 任务清单
1. **数据库设计**
   ```sql
   CREATE TABLE products (
       id BIGINT PRIMARY KEY AUTO_INCREMENT,
       title VARCHAR(100) NOT NULL,
       description TEXT,
       price DECIMAL(10,2) NOT NULL,
       original_price DECIMAL(10,2),
       category_id BIGINT,
       user_id BIGINT,
       status ENUM('AVAILABLE', 'SOLD', 'REMOVED') DEFAULT 'AVAILABLE',
       image_url VARCHAR(255),
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   );

   CREATE TABLE categories (
       id BIGINT PRIMARY KEY AUTO_INCREMENT,
       name VARCHAR(50) NOT NULL,
       description VARCHAR(200),
       parent_id BIGINT
   );
   ```

2. **功能实现**
   - 商品CRUD操作
   - 分类管理
   - 图片上传功能
   - 搜索功能实现
   - 分页查询

3. **前端页面**
   - 商品发布页面
   - 商品详情页面
   - 商品列表页面（带搜索和筛选）
   - 我的商品管理页面

#### 交付物
- 商品发布、编辑、删除功能
- 商品搜索和分类浏览
- 图片上传功能

### 阶段4：交易模块（预计2-3天）

#### 目标
实现完整的交易流程

#### 任务清单
1. **数据库设计**
   ```sql
   CREATE TABLE orders (
       id BIGINT PRIMARY KEY AUTO_INCREMENT,
       order_number VARCHAR(50) UNIQUE NOT NULL,
       buyer_id BIGINT,
       seller_id BIGINT,
       product_id BIGINT,
       total_price DECIMAL(10,2),
       status ENUM('PENDING', 'PAID', 'SHIPPED', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   );
   ```

2. **功能实现**
   - 订单创建逻辑
   - 订单状态流转
   - 支付状态管理
   - 交易记录查询

3. **前端页面**
   - 订单确认页面
   - 订单列表页面
   - 订单详情页面
   - 订单管理页面

#### 交付物
- 订单创建和状态管理
- 交易记录查看
- 订单流程控制

### 阶段5：消息模块（预计2-3天）

#### 目标
实现买卖双方的沟通功能

#### 任务清单
1. **数据库设计**
   ```sql
   CREATE TABLE messages (
       id BIGINT PRIMARY KEY AUTO_INCREMENT,
       sender_id BIGINT,
       receiver_id BIGINT,
       content TEXT,
       is_read BOOLEAN DEFAULT FALSE,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

2. **功能实现**
   - 私信发送与接收
   - 消息列表展示
   - 未读消息计数
   - 消息状态标记

3. **前端页面**
   - 消息列表页面
   - 聊天详情页面
   - 消息提醒组件

#### 交付物
- 私信收发功能
- 消息管理界面
- 未读消息提醒

### 阶段6：评价模块（预计1-2天）

#### 目标
建立用户信用体系

#### 任务清单
1. **数据库设计**
   ```sql
   CREATE TABLE reviews (
       id BIGINT PRIMARY KEY AUTO_INCREMENT,
       order_id BIGINT,
       reviewer_id BIGINT,
       reviewed_id BIGINT,
       rating INT CHECK (rating >= 1 AND rating <= 5),
       content TEXT,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

2. **功能实现**
   - 评价提交功能
   - 评价展示
   - 信用评分计算
   - 评价统计

3. **前端页面**
   - 评价提交页面
   - 用户评价列表
   - 信用评分展示

#### 交付物
- 评价系统
- 信用评分机制

### 阶段7：界面优化与测试（预计2-3天）

#### 目标
美化界面，确保功能稳定

#### 任务清单
1. **界面美化**
   - 使用Bootstrap优化所有页面
   - 响应式布局适配
   - 统一视觉风格
   - 添加加载动画

2. **功能测试**
   - 单元测试编写
   - 集成测试
   - 用户体验测试
   - Bug修复

3. **性能优化**
   - 数据库查询优化
   - 页面加载速度优化
   - 缓存策略

#### 交付物
- 美观的用户界面
- 稳定的功能
- 良好的用户体验

### 阶段8：部署与文档（预计1-2天）

#### 目标
将项目部署到GitHub，完善文档

#### 任务清单
1. **项目打包**
   - 配置Maven打包
   - 生成可执行JAR包
   - 测试部署

2. **文档编写**
   - 完善README文档
   - 添加API文档
   - 编写部署指南
   - 添加贡献指南

3. **GitHub上传**
   - 创建GitHub仓库
   - 上传代码
   - 创建Release版本
   - 设置GitHub Pages（可选）

#### 交付物
- 可运行的项目包
- 完整的项目文档
- GitHub仓库

## 学习路线建议

### 第一周：基础搭建
- **Day 1-2**: 环境设置 + 项目初始化
- **Day 3-5**: 用户模块开发
- **Day 6-7**: 商品模块基础功能

### 第二周：核心功能
- **Day 1-3**: 商品模块完善
- **Day 4-6**: 交易模块开发
- **Day 7**: 消息模块基础

### 第三周：完善与部署
- **Day 1-2**: 消息模块完善 + 评价模块
- **Day 3-5**: 界面优化 + 测试
- **Day 6-7**: 部署 + 文档

## 常见问题解答

### Q: 我需要学习哪些新知识？
A: 你需要了解：
1. Spring Boot基础概念
2. Spring Security基本配置
3. Spring Data JPA使用方法
4. Thymeleaf模板语法
5. Bootstrap CSS框架
6. AJAX异步请求

### Q: 遇到问题怎么办？
A: 可以：
1. 查看错误日志，定位问题
2. 搜索Stack Overflow
3. 查看Spring官方文档
4. 在GitHub上创建Issue
5. 我可以帮你调试代码

### Q: 如何保证代码质量？
A: 建议：
1. 遵循代码规范
2. 编写单元测试
3. 定期提交代码
4. 代码审查
5. 持续集成

## 下一步行动

1. **立即行动**: 完成环境设置（Maven安装、数据库创建）
2. **今日目标**: 创建第一个可运行的Spring Boot项目
3. **本周目标**: 完成用户模块，实现注册登录功能

## 联系与支持

如果你在开发过程中遇到任何问题，可以：
1. 查看项目文档
2. 搜索相关教程
3. 在GitHub上提问
4. 我会随时提供帮助

记住：编程是一项实践性很强的技能，多写代码、多调试、多总结，你一定能成功完成这个项目！