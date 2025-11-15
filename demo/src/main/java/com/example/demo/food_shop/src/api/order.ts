import request from './request'
import type { OrderDto, CreateOrderRequest } from '@/types'

export const orderApi = {
  // 从购物车创建订单
  createOrder(data: CreateOrderRequest) {
    return request.post<any, OrderDto>('/api/orders', data)
  },

  // 获取订单详情
  getOrderById(orderId: number) {
    return request.get<any, OrderDto>(`/api/orders/${orderId}`)
  },

  // 获取用户所有订单
  getUserOrders(userId: number) {
    return request.get<any, OrderDto[]>(`/api/orders/user/${userId}`)
  },

  // 获取所有订单 (管理员)
  getAllOrders() {
    return request.get<any, OrderDto[]>('/api/orders')
  },

  // 根据状态查询订单
  getOrdersByStatus(status: string) {
    return request.get<any, OrderDto[]>(`/api/orders/status/${status}`)
  },

  // 支付订单
  payOrder(orderId: number) {
    return request.patch<any, OrderDto>(`/api/orders/${orderId}/pay`)
  },

  // 发货
  shipOrder(orderId: number) {
    return request.patch<any, OrderDto>(`/api/orders/${orderId}/ship`)
  },

  // 确认收货
  deliverOrder(orderId: number) {
    return request.patch<any, OrderDto>(`/api/orders/${orderId}/deliver`)
  },

  // 取消订单
  cancelOrder(orderId: number) {
    return request.patch<any, OrderDto>(`/api/orders/${orderId}/cancel`)
  },

  // 获取订单项
  getOrderItems(orderId: number) {
    return request.get<any, any[]>(`/api/orders/${orderId}/items`)
  }
}
