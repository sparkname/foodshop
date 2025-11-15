# 前端无法显示商品信息 - 问题排查指南

## 已修复的问题

### 1. ✅ API 方法缺失
**问题**: `foodApi.getFoods()` 方法不存在
**修复**: 在 `food_shop/src/api/food.ts` 中添加了 `getFoods()` 方法

### 2. ✅ 后端端口配置错误
**问题**: Vite 代理配置指向 8080 端口，但后端运行在 8081
**修复**: 修改 `vite.config.ts` 中的代理端口为 8081

## 启动步骤

### 1. 启动后端服务（8081端口）
```powershell
cd c:\Users\lin\Desktop\Java_test1\demo
.\mvnw.cmd spring-boot:run
```

### 2. 启动前端服务（3000端口）
```powershell
cd c:\Users\lin\Desktop\Java_test1\demo\src\main\java\com\example\demo\food_shop
npm run dev
```

### 3. 访问前端
浏览器打开: http://localhost:3000

## 测试后端 API

### 测试商品列表接口
```powershell
# 测试获取所有商品
Invoke-WebRequest -Uri "http://localhost:8081/api/foods" -Method GET

# 测试获取可用商品
Invoke-WebRequest -Uri "http://localhost:8081/api/foods/available" -Method GET
```

## 常见问题排查

### 问题1: 后端连接失败
**症状**: 前端显示"获取食品列表失败"，控制台显示网络错误

**检查步骤**:
1. 确认后端是否启动: `netstat -ano | findstr :8081`
2. 检查数据库是否运行: MySQL 服务是否启动
3. 查看后端日志是否有错误

### 问题2: 数据库连接失败
**症状**: 后端启动失败，日志显示数据库连接错误

**解决方法**:
1. 确认 MySQL 服务运行: `Get-Service -Name MySQL*`
2. 检查数据库配置: `demo/src/main/resources/application.properties`
3. 确认数据库和表已创建: 运行 `test.sql`

### 问题3: 前端跨域问题
**症状**: 浏览器控制台显示 CORS 错误

**检查**:
- 后端 `FoodController.java` 已有 `@CrossOrigin(origins = "*")`
- 如果仍有问题，检查 Vite 的代理配置

### 问题4: 没有商品数据
**症状**: 接口正常但返回空数组

**解决**:
```sql
-- 在 MySQL 中执行
USE programe1db;
SELECT COUNT(*) FROM food;
-- 如果返回 0，重新运行 test.sql 导入数据
```

## 浏览器控制台检查

打开浏览器开发者工具（F12），查看：

1. **Console 标签**: 查看 JavaScript 错误
2. **Network 标签**: 查看 API 请求状态
   - 请求地址应该是: `http://localhost:3000/api/foods/available`
   - 代理后会转发到: `http://localhost:8081/api/foods/available`
   - 状态码应该是: 200 OK

## 预期结果

成功后应该看到：
- 前端页面显示 20 个商品（主食、小吃、饮料、甜点）
- 每个商品有名称、描述、价格、库存
- 可以点击"加入购物车"按钮

## 数据库中的测试商品

数据库中已有以下类型的商品：
- 主食: 黄焖鸡米饭、麻辣烫、沙县小吃等 (5种)
- 小吃: 烤冷面、煎饼果子、肉夹馍等 (5种)
- 饮料: 珍珠奶茶、柠檬茶、可乐等 (5种)
- 甜点: 芒果班戟、双皮奶、提拉米苏等 (5种)

## 快速验证命令

```powershell
# 1. 检查后端端口
netstat -ano | findstr :8081

# 2. 检查前端端口
netstat -ano | findstr :3000

# 3. 测试后端 API
curl http://localhost:8081/api/foods/available

# 4. 检查 MySQL 服务
Get-Service -Name MySQL*
```
