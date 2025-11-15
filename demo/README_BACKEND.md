# 美食商城后端系统

## 项目结构

```
demo/
├── src/main/java/com/example/
│   ├── Repository/          # 数据访问层
│   │   ├── CartRepository.java
│   │   ├── CartItemRepository.java
│   │   ├── FoodRepository.java
│   │   ├── OrderRepository.java
│   │   ├── OrderItemRepository.java
│   │   └── UserRepository.java
│   │
│   └── demo/
│       ├── Programe1Application.java  # 启动类
│       │
│       ├── Entitydto/       # 实体类
│       │   ├── Cart.java
│       │   ├── CartItem.java
│       │   ├── Food.java
│       │   ├── Order.java
│       │   ├── OrderItem.java
│       │   └── User.java
│       │
│       ├── Service/         # 业务逻辑层
│       │   ├── CartService.java
│       │   ├── FoodService.java
│       │   ├── OrderService.java
│       │   └── UserService.java
│       │
│       └── Controller/      # 控制器层
│           ├── CartController.java
│           ├── FoodController.java
│           ├── OrderController.java
│           └── UserController.java
```

## 核心功能模块

### 1. 用户模块 (User)

**实体属性:**
- 用户ID、用户名、密码
- 邮箱、手机号、地址
- 真实姓名、创建/更新时间

**主要功能:**
- ✅ 用户注册 (POST `/api/users/register`)
- ✅ 用户登录 (POST `/api/users/login`)
- ✅ 获取用户信息 (GET `/api/users/{id}`)
- ✅ 更新用户信息 (PUT `/api/users/{id}`)
- ✅ 修改密码 (PATCH `/api/users/{id}/password`)
- ✅ 删除用户 (DELETE `/api/users/{id}`)

### 2. 商品模块 (Food)

**实体属性:**
- 商品ID、名称、描述
- 价格、库存、分类
- 图片URL、是否可用
- 创建/更新时间

**主要功能:**
- ✅ 获取所有商品 (GET `/api/foods`)
- ✅ 获取可用商品 (GET `/api/foods/available`)
- ✅ 根据ID获取商品 (GET `/api/foods/{id}`)
- ✅ 根据分类获取商品 (GET `/api/foods/category/{categoryId}`)
- ✅ 搜索商品 (GET `/api/foods/search?name={name}`)
- ✅ 添加商品 (POST `/api/foods`)
- ✅ 更新商品 (PUT `/api/foods/{id}`)
- ✅ 删除商品 (DELETE `/api/foods/{id}`)
- ✅ 更新库存 (PATCH `/api/foods/{id}/stock`)
- ✅ 上架/下架商品 (PATCH `/api/foods/{id}/enable` | `/disable`)

### 3. 购物车模块 (Cart)

**实体关系:**
- Cart (购物车) - 一个用户一个购物车
- CartItem (购物车项) - 购物车中的商品

**主要功能:**
- ✅ 获取购物车 (GET `/api/cart/{userId}`)
- ✅ 获取购物车商品列表 (GET `/api/cart/{userId}/items`)
- ✅ 添加商品到购物车 (POST `/api/cart/{userId}/items`)
- ✅ 更新商品数量 (PUT `/api/cart/{userId}/items/{cartItemId}`)
- ✅ 删除购物车商品 (DELETE `/api/cart/{userId}/items/{cartItemId}`)
- ✅ 清空购物车 (DELETE `/api/cart/{userId}`)
- ✅ 计算购物车总价 (GET `/api/cart/{userId}/total`)
- ✅ 检查购物车库存 (GET `/api/cart/{userId}/check-stock`)

**业务逻辑:**
- 添加商品时自动检查库存
- 如果商品已存在则增加数量
- 自动创建购物车(如果不存在)
- 数量为0时自动删除商品

### 4. 订单模块 (Order)

**实体关系:**
- Order (订单) - 用户的购买记录
- OrderItem (订单项) - 订单中的商品

**订单状态:**
- PENDING (待支付)
- PAID (已支付)
- SHIPPED (已发货)
- DELIVERED (已完成)
- CANCELLED (已取消)

**主要功能:**
- ✅ 从购物车创建订单 (POST `/api/orders`)
- ✅ 获取订单详情 (GET `/api/orders/{orderId}`)
- ✅ 获取用户所有订单 (GET `/api/orders/user/{userId}`)
- ✅ 获取所有订单 (GET `/api/orders`)
- ✅ 根据状态查询订单 (GET `/api/orders/status/{status}`)
- ✅ 支付订单 (PATCH `/api/orders/{orderId}/pay`)
- ✅ 发货 (PATCH `/api/orders/{orderId}/ship`)
- ✅ 确认收货 (PATCH `/api/orders/{orderId}/deliver`)
- ✅ 取消订单 (PATCH `/api/orders/{orderId}/cancel`)
- ✅ 获取订单项 (GET `/api/orders/{orderId}/items`)

**业务逻辑:**
- 创建订单时自动生成订单号
- 自动计算订单总价
- 创建订单后扣减库存
- 创建订单后清空购物车
- 取消订单时恢复库存
- 状态流转控制(只能按顺序流转)

## 技术栈

- **框架:** Spring Boot
- **ORM:** Spring Data JPA + Hibernate
- **数据库:** (需配置 application.properties)
- **依赖注入:** Spring IoC
- **事务管理:** @Transactional
- **数据校验:** Jakarta Validation

## 数据库表设计

### users (用户表)
- id, username, password, email, phone, address, real_name
- created_at, updated_at

### foods (商品表)
- id, name, description, price, stock, category, image_url, available
- created_at, updated_at

### carts (购物车表)
- id, user_id (外键)
- created_at, updated_at

### cart_items (购物车项表)
- id, cart_id (外键), food_id (外键), quantity, price
- created_at

### orders (订单表)
- id, user_id (外键), order_number, total_amount, status
- delivery_address, contact_phone, remark
- created_at, updated_at

### order_items (订单项表)
- id, order_id (外键), food_id (外键), quantity, price, subtotal

## 业务流程

### 完整购物流程

```
1. 用户注册/登录
   POST /api/users/register
   POST /api/users/login

2. 浏览商品
   GET /api/foods/available
   GET /api/foods/search?name=xxx

3. 添加到购物车
   POST /api/cart/{userId}/items
   参数: foodId, quantity

4. 查看购物车
   GET /api/cart/{userId}/items
   GET /api/cart/{userId}/total

5. 修改购物车
   PUT /api/cart/{userId}/items/{cartItemId}
   DELETE /api/cart/{userId}/items/{cartItemId}

6. 创建订单
   POST /api/orders
   Body: {userId, deliveryAddress, contactPhone, remark}

7. 支付订单
   PATCH /api/orders/{orderId}/pay

8. 查看订单
   GET /api/orders/user/{userId}
```

## 错误处理

所有 Service 层方法都会抛出以下异常:
- `RuntimeException`: 资源不存在
- `IllegalArgumentException`: 参数错误、业务规则违反

Controller 层统一捕获异常并返回:
- `200 OK`: 成功
- `201 CREATED`: 创建成功
- `400 BAD_REQUEST`: 参数错误
- `401 UNAUTHORIZED`: 认证失败
- `404 NOT_FOUND`: 资源不存在
- `500 INTERNAL_SERVER_ERROR`: 服务器错误

## API 特性

- ✅ RESTful 风格
- ✅ 统一异常处理
- ✅ 事务管理
- ✅ 数据校验
- ✅ 跨域支持 (@CrossOrigin)
- ✅ 业务逻辑封装

## 下一步建议

### 功能增强
- [ ] 添加用户认证(JWT Token)
- [ ] 添加角色权限管理
- [ ] 添加商品分类管理
- [ ] 添加订单评价功能
- [ ] 添加优惠券功能
- [ ] 添加支付接口集成

### 技术优化
- [ ] 添加 Redis 缓存
- [ ] 添加消息队列(订单异步处理)
- [ ] 添加日志系统
- [ ] 添加接口文档(Swagger)
- [ ] 添加单元测试
- [ ] 优化异常处理机制

## 配置说明

需要在 `application.properties` 中配置:

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/food_shop
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# 服务器配置
server.port=8080
```

## 启动项目

```bash
# 进入项目目录
cd demo

# Maven 启动
./mvnw spring-boot:run

# 或者
mvn clean install
java -jar target/demo-0.0.1-SNAPSHOT.jar
```


cd C:\Users\lin\Desktop\Java_test1\demo\src\main\java\com\example\demo\food_shop 
npm run dev
## 测试 API

可以使用 Postman 或 curl 测试 API:

```bash
# 注册用户
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456","email":"test@example.com"}'

# 获取商品列表
curl http://localhost:8080/api/foods/available

# 添加到购物车
curl -X POST "http://localhost:8080/api/cart/1/items?foodId=1&quantity=2"
```

---

**开发完成日期:** 2025年11月13日
**状态:** ✅ 后端业务逻辑已完成
