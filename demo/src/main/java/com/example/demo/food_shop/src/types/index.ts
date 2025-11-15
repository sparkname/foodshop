// DTO 类型定义
export interface UserDto {
  id: number
  username: string
  email: string
  phone: string
  address: string
  realName: string
}

export interface FoodDto {
  id: number
  name: string
  description: string
  price: number
  stock: number
  category: string
  imageUrl: string
  available: boolean
}

export interface CartItemDto {
  id: number
  foodId: number
  foodName: string
  price: number
  quantity: number
  subtotal: number
}

export interface CartDto {
  id: number
  userId: number
  items: CartItemDto[]
  totalAmount: number
}

export interface OrderItemDto {
  id: number
  orderId: number
  foodId: number
  foodName: string
  quantity: number
  price: number
  subtotal: number
}

export interface OrderDto {
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

// 请求类型定义
export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
  phone?: string
  address?: string
  realName?: string
}

export interface ChangePasswordRequest {
  oldPassword: string
  newPassword: string
}

export interface AddToCartRequest {
  foodId: number
  quantity: number
}

export interface UpdateCartItemRequest {
  quantity: number
}

export interface CreateOrderRequest {
  userId: number
  deliveryAddress: string
  contactPhone: string
  remark?: string
}

