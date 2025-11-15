# 前端 API 使用文档

## 概述

本项目前端 API 已完全对接后端 RESTful 接口,所有请求都通过 axios 统一管理。

## 基础配置

### API 基础路径
```typescript
baseURL: '/api'
```

### 请求拦截器
- 自动添加 Token (从 localStorage 获取)
- 请求头默认设置为 `application/json`

### 响应拦截器
- 自动解析响应数据
- 统一错误处理

## API 模块

### 1. 用户 API (userApi)

```typescript
import { userApi } from '@/api'

// 用户注册
userApi.register({
  username: 'test',
  password: '123456',
  email: 'test@example.com',
  phone: '13800138000',
  address: '测试地址',
  realName: '测试用户'
})

// 用户登录
userApi.login({
  username: 'test',
  password: '123456'
})

// 获取用户信息
userApi.getUserInfo(1)

// 更新用户信息
userApi.updateUser(1, {
  address: '新地址',
  phone: '13900139000'
})

// 修改密码
userApi.changePassword(1, {
  oldPassword: '123456',
  newPassword: '654321'
})

// 删除用户 (管理员)
userApi.deleteUser(1)

// 获取所有用户 (管理员)
userApi.getAllUsers()
```

### 2. 商品 API (foodApi)

```typescript
import { foodApi } from '@/api'

// 获取所有商品
foodApi.getAllFoods()

// 获取所有可用商品
foodApi.getAvailableFoods()

// 根据 ID 获取商品
foodApi.getFoodById(1)

// 搜索商品
foodApi.searchFoods('汉堡')

// 根据分类获取商品
foodApi.getFoodsByCategory(1)

// 添加商品 (管理员)
foodApi.addFood({
  name: '汉堡',
  description: '美味汉堡',
  price: 25.00,
  stock: 100,
  category: '快餐',
  imageUrl: '/images/burger.jpg',
  available: true
})

// 更新商品 (管理员)
foodApi.updateFood(1, {
  price: 28.00,
  stock: 80
})

// 删除商品 (管理员)
foodApi.deleteFood(1)

// 更新库存 (管理员)
foodApi.updateStock(1, 150)

// 上架商品 (管理员)
foodApi.enableFood(1)

// 下架商品 (管理员)
foodApi.disableFood(1)
```

### 3. 购物车 API (cartApi)

```typescript
import { cartApi } from '@/api'

const userId = 1

// 获取购物车
cartApi.getCart(userId)

// 获取购物车商品列表
cartApi.getCartItems(userId)

// 添加商品到购物车
cartApi.addToCart(userId, {
  foodId: 1,
  quantity: 2
})

// 更新购物车商品数量
cartApi.updateCartItem(userId, 1, 3)

// 删除购物车商品
cartApi.removeCartItem(userId, 1)

// 清空购物车
cartApi.clearCart(userId)

// 计算购物车总价
cartApi.getCartTotal(userId)

// 检查购物车库存
cartApi.checkCartStock(userId)
```

### 4. 订单 API (orderApi)

```typescript
import { orderApi } from '@/api'

// 从购物车创建订单
orderApi.createOrder({
  userId: 1,
  deliveryAddress: '北京市朝阳区xxx',
  contactPhone: '13800138000',
  remark: '请尽快配送'
})

// 获取订单详情
orderApi.getOrderById(1)

// 获取用户所有订单
orderApi.getUserOrders(1)

// 获取所有订单 (管理员)
orderApi.getAllOrders()

// 根据状态查询订单
orderApi.getOrdersByStatus('PENDING')
// 状态: PENDING | PAID | SHIPPED | DELIVERED | CANCELLED

// 支付订单
orderApi.payOrder(1)

// 发货 (管理员)
orderApi.shipOrder(1)

// 确认收货
orderApi.deliverOrder(1)

// 取消订单
orderApi.cancelOrder(1)

// 获取订单项
orderApi.getOrderItems(1)
```

## 统一导入

```typescript
// 方式1: 按需导入
import { userApi, foodApi, cartApi, orderApi } from '@/api'

// 方式2: 默认导入
import api from '@/api'
api.user.login(...)
api.food.getAllFoods()
api.cart.getCart(...)
api.order.createOrder(...)
```

## 类型定义

### 用户相关类型

```typescript
interface UserDto {
  id: number
  username: string
  email: string
  phone: string
  address: string
  realName: string
}

interface LoginRequest {
  username: string
  password: string
}

interface RegisterRequest {
  username: string
  password: string
  email: string
  phone?: string
  address?: string
  realName?: string
}

interface ChangePasswordRequest {
  oldPassword: string
  newPassword: string
}
```

### 商品相关类型

```typescript
interface FoodDto {
  id: number
  name: string
  description: string
  price: number
  stock: number
  category: string
  imageUrl: string
  available: boolean
}
```

### 购物车相关类型

```typescript
interface CartItemDto {
  id: number
  foodId: number
  foodName: string
  price: number
  quantity: number
  subtotal: number
}

interface CartDto {
  id: number
  userId: number
  items: CartItemDto[]
  totalAmount: number
}

interface AddToCartRequest {
  foodId: number
  quantity: number
}
```

### 订单相关类型

```typescript
interface OrderItemDto {
  id: number
  orderId: number
  foodId: number
  foodName: string
  quantity: number
  price: number
  subtotal: number
}

interface OrderDto {
  id: number
  userId: number
  orderNumber: string
  totalAmount: number
  status: 'PENDING' | 'PAID' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED'
  deliveryAddress: string
  contactPhone: string
  remark: string
  orderItems: OrderItemDto[]
  createdAt: string
  updatedAt: string
}

interface CreateOrderRequest {
  userId: number
  deliveryAddress: string
  contactPhone: string
  remark?: string
}
```

## Vue 组件中使用示例

### 商品列表页面

```vue
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { foodApi } from '@/api'
import type { FoodDto } from '@/types'

const foods = ref<FoodDto[]>([])
const loading = ref(false)

const loadFoods = async () => {
  loading.value = true
  try {
    foods.value = await foodApi.getAvailableFoods()
  } catch (error) {
    console.error('加载商品失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadFoods()
})
</script>
```

### 购物车页面

```vue
<script setup lang="ts">
import { ref, computed } from 'vue'
import { cartApi } from '@/api'

const userId = ref(1) // 从用户状态获取
const cartItems = ref([])

const loadCart = async () => {
  try {
    const items = await cartApi.getCartItems(userId.value)
    cartItems.value = items
  } catch (error) {
    console.error('加载购物车失败:', error)
  }
}

const addToCart = async (foodId: number, quantity: number) => {
  try {
    await cartApi.addToCart(userId.value, { foodId, quantity })
    await loadCart()
  } catch (error) {
    console.error('添加到购物车失败:', error)
  }
}

const updateQuantity = async (cartItemId: number, quantity: number) => {
  try {
    await cartApi.updateCartItem(userId.value, cartItemId, quantity)
    await loadCart()
  } catch (error) {
    console.error('更新数量失败:', error)
  }
}
</script>
```

### 创建订单

```vue
<script setup lang="ts">
import { ref } from 'vue'
import { orderApi } from '@/api'
import { useRouter } from 'vue-router'

const router = useRouter()
const userId = ref(1)
const orderForm = ref({
  deliveryAddress: '',
  contactPhone: '',
  remark: ''
})

const createOrder = async () => {
  try {
    const order = await orderApi.createOrder({
      userId: userId.value,
      ...orderForm.value
    })
    alert('订单创建成功!')
    router.push(`/orders/${order.id}`)
  } catch (error) {
    console.error('创建订单失败:', error)
  }
}
</script>
```

## 错误处理

所有 API 调用都应该使用 try-catch 处理错误:

```typescript
try {
  const result = await userApi.login(loginData)
  // 处理成功
} catch (error: any) {
  // 处理错误
  if (error.response) {
    // 服务器返回错误
    console.error('错误:', error.response.data)
  } else {
    // 网络错误或其他错误
    console.error('请求失败:', error.message)
  }
}
```

## 后端 API 对应表

| 前端方法 | HTTP 方法 | 后端路径 | 说明 |
|---------|----------|----------|------|
| userApi.register | POST | /api/users/register | 用户注册 |
| userApi.login | POST | /api/users/login | 用户登录 |
| foodApi.getAllFoods | GET | /api/foods | 获取所有商品 |
| foodApi.getAvailableFoods | GET | /api/foods/available | 获取可用商品 |
| cartApi.getCart | GET | /api/cart/{userId} | 获取购物车 |
| cartApi.addToCart | POST | /api/cart/{userId}/items | 添加到购物车 |
| orderApi.createOrder | POST | /api/orders | 创建订单 |
| orderApi.payOrder | PATCH | /api/orders/{orderId}/pay | 支付订单 |

## 注意事项

1. **用户 ID**: 大部分接口需要传入 userId,实际项目中应该从用户登录状态中获取
2. **Token**: 登录成功后应该保存 token 到 localStorage,请求拦截器会自动添加到请求头
3. **错误处理**: 所有 API 调用都应该包含错误处理逻辑
4. **类型安全**: 使用 TypeScript 类型定义确保类型安全
5. **状态管理**: 建议使用 Pinia 管理全局状态(用户信息、购物车等)

## 完整业务流程示例

```typescript
// 1. 用户注册
await userApi.register({
  username: 'newuser',
  password: '123456',
  email: 'newuser@example.com'
})

// 2. 用户登录
const user = await userApi.login({
  username: 'newuser',
  password: '123456'
})
const userId = user.id
localStorage.setItem('userId', userId.toString())

// 3. 浏览商品
const foods = await foodApi.getAvailableFoods()

// 4. 添加到购物车
await cartApi.addToCart(userId, { foodId: 1, quantity: 2 })
await cartApi.addToCart(userId, { foodId: 2, quantity: 1 })

// 5. 查看购物车
const cart = await cartApi.getCart(userId)
const total = await cartApi.getCartTotal(userId)

// 6. 检查库存
const hasStock = await cartApi.checkCartStock(userId)

// 7. 创建订单
const order = await orderApi.createOrder({
  userId,
  deliveryAddress: '北京市朝阳区xxx',
  contactPhone: '13800138000'
})

// 8. 支付订单
await orderApi.payOrder(order.id)

// 9. 查看订单
const userOrders = await orderApi.getUserOrders(userId)
```

---

**更新日期:** 2025年11月13日
**状态:** ✅ 前后端 API 已完全对接
