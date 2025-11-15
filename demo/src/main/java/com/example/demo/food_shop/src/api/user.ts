import request from './request'
import type { UserDto, LoginRequest, RegisterRequest, ChangePasswordRequest } from '@/types'

export const userApi = {
  // 用户注册
  register(data: RegisterRequest) {
    return request.post<any, UserDto>('/api/users/register', data)
  },

  // 用户登录
  login(data: LoginRequest) {
    return request.post<any, UserDto>('/api/users/login', data)
  },

  // 获取用户信息
  getUserInfo(id: number) {
    return request.get<any, UserDto>(`/api/users/${id}`)
  },

  // 获取所有用户 (管理员)
  getAllUsers() {
    return request.get<any, UserDto[]>('/api/users')
  },

  // 更新用户信息
  updateUser(id: number, data: Partial<UserDto>) {
    return request.put<any, UserDto>(`/api/users/${id}`, data)
  },

  // 修改密码
  changePassword(id: number, data: ChangePasswordRequest) {
    return request.patch<any, UserDto>(`/api/users/${id}/password`, data)
  },

  // 删除用户 (管理员)
  deleteUser(id: number) {
    return request.delete<any, void>(`/api/users/${id}`)
  }
}
