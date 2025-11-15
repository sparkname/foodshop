import { createRouter, createWebHistory } from 'vue-router'
import FoodList from '@/views/FoodList.vue'
import Cart from '@/views/Cart.vue'
import UserProfile from '@/views/UserProfile.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: FoodList,
      meta: { title: '美食广场' }
    },
    {
      path: '/cart',
      name: 'cart',
      component: Cart,
      meta: { title: '购物车' }
    },
    {
      path: '/profile',
      name: 'profile',
      component: UserProfile,
      meta: { title: '个人信息' }
    }
  ]
})

// 路由守卫 - 设置页面标题
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title as string
  }
  next()
})

export default router
