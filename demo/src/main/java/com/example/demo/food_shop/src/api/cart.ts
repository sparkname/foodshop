import request from './request'
import type { CartDto, AddToCartRequest, UpdateCartItemRequest } from '@/types'

export const cartApi = {
  // 获取购物车
  getCart(userId: number) {
    return request.get<any, CartDto>(`/api/cart/${userId}`)
  },

  // 获取购物车商品列表
  getCartItems(userId: number) {
    return request.get<any, any[]>(`/api/cart/${userId}/items`)
  },

  // 添加商品到购物车
  addToCart(userId: number, data: AddToCartRequest) {
    return request.post<any, CartDto>(`/api/cart/${userId}/items?foodId=${data.foodId}&quantity=${data.quantity}`)
  },

  // 更新购物车商品数量
  updateCartItem(userId: number, cartItemId: number, quantity: number) {
    return request.put<any, CartDto>(`/api/cart/${userId}/items/${cartItemId}?quantity=${quantity}`)
  },

  // 删除购物车商品
  removeCartItem(userId: number, cartItemId: number) {
    return request.delete<any, CartDto>(`/api/cart/${userId}/items/${cartItemId}`)
  },

  // 清空购物车
  clearCart(userId: number) {
    return request.delete<any, void>(`/api/cart/${userId}`)
  },

  // 计算购物车总价
  getCartTotal(userId: number) {
    return request.get<any, number>(`/api/cart/${userId}/total`)
  },

  // 检查购物车库存
  checkCartStock(userId: number) {
    return request.get<any, boolean>(`/api/cart/${userId}/check-stock`)
  }
}
