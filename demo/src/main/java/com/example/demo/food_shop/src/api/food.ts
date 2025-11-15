import request from './request'
import type { FoodDto } from '@/types'

export const foodApi = {
  // 获取食品列表（支持搜索和分类筛选）
  getFoods(params?: { category?: string; keyword?: string }) {
    if (params?.keyword) {
      return request.get<any, FoodDto[]>('/api/foods/search', { params: { name: params.keyword } })
    }
    // 默认获取所有可用食品
    return request.get<any, FoodDto[]>('/api/foods/available')
  },

  // 获取所有食品
  getAllFoods() {
    return request.get<any, FoodDto[]>('/api/foods')
  },

  // 获取所有可用食品
  getAvailableFoods() {
    return request.get<any, FoodDto[]>('/api/foods/available')
  },

  // 根据 ID 获取食品详情
  getFoodById(id: number) {
    return request.get<any, FoodDto>(`/api/foods/${id}`)
  },

  // 搜索食品
  searchFoods(name: string) {
    return request.get<any, FoodDto[]>('/api/foods/search', { params: { name } })
  },

  // 根据分类获取食品
  getFoodsByCategory(categoryId: number) {
    return request.get<any, FoodDto[]>(`/api/foods/category/${categoryId}`)
  },

  // 添加食品 (管理员)
  addFood(data: Partial<FoodDto>) {
    return request.post<any, FoodDto>('/api/foods', data)
  },

  // 更新食品 (管理员)
  updateFood(id: number, data: Partial<FoodDto>) {
    return request.put<any, FoodDto>(`/api/foods/${id}`, data)
  },

  // 删除食品 (管理员)
  deleteFood(id: number) {
    return request.delete<any, void>(`/api/foods/${id}`)
  },

  // 更新库存 (管理员)
  updateStock(id: number, stock: number) {
    return request.patch<any, FoodDto>(`/api/foods/${id}/stock`, null, { params: { stock } })
  },

  // 上架食品 (管理员)
  enableFood(id: number) {
    return request.patch<any, FoodDto>(`/api/foods/${id}/enable`)
  },

  // 下架食品 (管理员)
  disableFood(id: number) {
    return request.patch<any, FoodDto>(`/api/foods/${id}/disable`)
  }
}
