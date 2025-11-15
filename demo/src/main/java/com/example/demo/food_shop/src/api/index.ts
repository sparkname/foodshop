// 统一导出所有 API
import { userApi } from './user'
import { foodApi } from './food'
import { cartApi } from './cart'
import { orderApi } from './order'
// 也可以使用默认导出
export { userApi, foodApi, cartApi, orderApi }

export default {
  user: userApi,
  food: foodApi,
  cart: cartApi,
  order: orderApi
}


export default api