# 校园二手交易系统 (Campus Trading System)

一个基于 Spring Boot 的校园二手物品交易平台，支持用户注册登录、商品发布与搜索、下单交易、买卖双方私信、交易评价等完整功能。适合作为 Java Web 学习项目。

## 技术栈

| 层次 | 技术 |
|------|------|
| 后端框架 | Spring Boot 2.7.18 |
| 安全 | Spring Security 5 |
| 数据访问 | Spring Data JPA (Hibernate) |
| 数据库 | MySQL 8.0 |
| 模板引擎 | Thymeleaf |
| 前端 | Bootstrap 5 + Bootstrap Icons |
| 构建 | Maven |
| Java 版本 | 17+ |

## 功能清单

- **用户模块**：注册、登录、退出、个人中心、修改资料、修改密码
- **商品模块**：发布、编辑、下架、图片上传、分类、关键词搜索、分页
- **订单模块**：下单、卖家确认、买家确认收货、取消订单、订单状态流转
- **消息模块**：买卖双方私信、会话记录、未读消息提醒
- **评价模块**：交易完成后互评、评分与信用展示、用户主页

## 快速开始

### 1. 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0

### 2. 创建数据库
```sql
CREATE DATABASE campus_trading CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
表结构由 JPA 自动创建（`spring.jpa.hibernate.ddl-auto=update`）。

### 3. 配置数据库连接
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_trading?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8
    username: root
    password: 你的密码
```

### 4. 启动项目
```bash
mvn spring-boot:run
```

访问 http://localhost:8080

### 5. 演示账号
- 用户名：`admin`
- 密码：`123456`

## 项目结构

```
campus-trading-system/
├── src/main/java/com/example/campus/
│   ├── CampusTradingApplication.java   # 启动类
│   ├── config/                         # 安全、Web、数据初始化配置
│   ├── controller/                     # 控制器（处理HTTP请求）
│   ├── entity/                         # 实体类（对应数据库表）
│   ├── repository/                     # 数据访问层
│   ├── service/                        # 业务逻辑层
│   ├── dto/                            # 数据传输对象
│   └── util/                           # 工具类
├── src/main/resources/
│   ├── application.yml                 # 配置文件
│   ├── templates/                      # Thymeleaf 页面
│   │   ├── fragments/common.html       # 公共导航/页脚
│   │   └── ...                         # 各功能页面
│   └── static/                         # 静态资源
├── uploads/                            # 商品图片上传目录
└── pom.xml
```

## 页面一览

| 路径 | 说明 | 是否需登录 |
|------|------|-----------|
| `/` | 首页 | 否 |
| `/products` | 商品列表 / 搜索 | 否 |
| `/products/{id}` | 商品详情 | 否 |
| `/products/new` | 发布商品 | 是 |
| `/products/my` | 我的商品 | 是 |
| `/orders/bought` | 我买到的 | 是 |
| `/orders/sold` | 我卖出的 | 是 |
| `/messages` | 我的消息 | 是 |
| `/profile` | 个人中心 | 是 |
| `/users/{id}` | 用户主页 | 否 |

## 订单状态流转

```
待确认(PENDING) --卖家确认--> 交易中(TRADING) --买家收货--> 已完成(COMPLETED)
     |                              |
     +---------- 取消 -------------+--> 已取消(CANCELLED)
```

## 开发计划

- [x] 阶段1：项目初始化
- [x] 阶段2：用户模块
- [x] 阶段3：商品模块
- [x] 阶段4：交易模块
- [x] 阶段5：消息模块
- [x] 阶段6：评价模块
- [x] 阶段7：界面优化
- [ ] 阶段8：部署与持续完善

## 后续可扩展方向

- 使用 WebSocket 实现实时聊天
- 引入 Redis 缓存热门商品
- 增加管理员后台（用户/商品管理）
- 接入支付宝/微信沙箱支付
- 使用 Docker 容器化部署
- 编写单元测试与集成测试

## License

本项目仅供学习交流使用。
