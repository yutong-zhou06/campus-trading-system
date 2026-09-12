# 开发环境设置指南

## 已检测到的环境

- ✅ **Java**: JDK 22.0.2 (已安装)
- ❌ **Maven**: 未安装
- ✅ **MySQL**: 8.0.45 (已安装)

## 第一步：安装Maven

### Windows安装步骤：

1. **下载Maven**
   - 访问 https://maven.apache.org/download.cgi
   - 下载 "Binary zip archive" (apache-maven-3.9.6-bin.zip)

2. **解压安装**
   - 将zip文件解压到 `C:\Program Files\Apache\maven` (或你喜欢的目录)
   - 确保目录结构为：`C:\Program Files\Apache\maven\apache-maven-3.9.6`

3. **配置环境变量**
   - 右键"此电脑" → 属性 → 高级系统设置 → 环境变量
   - 新建系统变量：
     - 变量名：`MAVEN_HOME`
     - 变量值：`C:\Program Files\Apache\maven\apache-maven-3.9.6`
   - 编辑系统变量 `Path`，添加：`%MAVEN_HOME%\bin`

4. **验证安装**
   - 打开新的命令行窗口，运行：
   ```
   mvn -version
   ```
   - 应该显示Maven版本信息

## 第二步：配置MySQL数据库

1. **启动MySQL服务**
   - 打开命令行，运行：
   ```
   mysql -u root -p
   ```
   - 输入你的MySQL root密码

2. **创建项目数据库**
   ```sql
   CREATE DATABASE campus_trading;
   CREATE USER 'campus_user'@'localhost' IDENTIFIED BY 'campus_password';
   GRANT ALL PRIVILEGES ON campus_trading.* TO 'campus_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **验证数据库**
   ```sql
   SHOW DATABASES;
   USE campus_trading;
   SHOW TABLES;
   ```

## 第三步：选择IDE

### 推荐选项：

1. **IntelliJ IDEA** (推荐)
   - 下载社区版：https://www.jetbrains.com/idea/download/
   - 安装后配置JDK和Maven

2. **VS Code** (轻量级)
   - 安装Java Extension Pack扩展
   - 安装Spring Boot Extension Pack

3. **Eclipse** (传统选择)
   - 下载Eclipse IDE for Java Developers

## 第四步：创建GitHub账户

1. 访问 https://github.com/
2. 注册账户（如果还没有）
3. 安装Git：https://git-scm.com/download/win
4. 配置Git：
   ```bash
   git config --global user.name "你的名字"
   git config --global user.email "你的邮箱"
   ```

## 下一步

完成上述设置后，告诉我：
1. Maven是否安装成功？
2. 数据库是否创建成功？
3. 你选择哪个IDE？

然后我将指导你创建Spring Boot项目骨架。