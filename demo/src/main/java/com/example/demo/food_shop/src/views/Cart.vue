<template>
  <div class="cart">
    <div class="cart-header">
      <h1>购物车</h1>
      <button v-if="cart && cart.items.length > 0" @click="clearCart" class="clear-btn">
        清空购物车
      </button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else-if="cart && cart.items.length > 0" class="cart-content">
      <div class="cart-items">
        <div v-for="item in cart.items" :key="item.id" class="cart-item">
          <div class="item-info">
            <h3>{{ item.foodName }}</h3>
            <p class="item-price">单价: ¥{{ item.price.toFixed(2) }}</p>
          </div>
          <div class="item-actions">
            <button @click="updateQuantity(item, item.quantity - 1)" :disabled="item.quantity <= 1">
              -
            </button>
            <span class="quantity">{{ item.quantity }}</span>
            <button @click="updateQuantity(item, item.quantity + 1)">+</button>
          </div>
          <div class="item-subtotal">
            <p>小计</p>
            <p class="subtotal-amount">¥{{ item.subtotal.toFixed(2) }}</p>
          </div>
          <button @click="removeItem(item.id)" class="remove-btn">删除</button>
        </div>
      </div>

      <div class="cart-summary">
        <div class="summary-content">
          <h2>订单汇总</h2>
          <div class="summary-row">
            <span>商品数量:</span>
            <span>{{ totalItems }} 件</span>
          </div>
          <div class="summary-row total">
            <span>总计:</span>
            <span class="total-amount">¥{{ cart.totalAmount.toFixed(2) }}</span>
          </div>
          <button @click="checkout" class="checkout-btn">去结算</button>
        </div>
      </div>
    </div>

    <div v-else class="empty-cart">
      <div class="empty-icon">🛒</div>
      <p>购物车是空的</p>
      <button @click="goToFoodList" class="go-shopping-btn">去购物</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { cartApi } from '@/api/cart'
import type { CartDto, CartItemDto } from '@/types'

const router = useRouter()
const cart = ref<CartDto | null>(null)
const loading = ref(false)

const totalItems = computed(() => {
  return cart.value?.items.reduce((sum, item) => sum + item.quantity, 0) || 0
})

const fetchCart = async () => {
  loading.value = true
  try {
    // 使用当前用户的购物车，或者使用固定 userId
    cart.value = await cartApi.getCurrentCart()
  } catch (error) {
    console.error('获取购物车失败:', error)
    alert('获取购物车失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const updateQuantity = async (item: CartItemDto, newQuantity: number) => {
  if (newQuantity < 1) return

  try {
    await cartApi.updateCartItem(item.id, { quantity: newQuantity })
    await fetchCart()
  } catch (error) {
    console.error('更新数量失败:', error)
    alert('更新失败，请稍后重试')
  }
}

const removeItem = async (itemId: number) => {
  if (!confirm('确定要删除这个商品吗？')) return

  try {
    await cartApi.removeCartItem(itemId)
    await fetchCart()
  } catch (error) {
    console.error('删除商品失败:', error)
    alert('删除失败，请稍后重试')
  }
}

const clearCart = async () => {
  if (!confirm('确定要清空购物车吗？')) return

  try {
    if (cart.value) {
      await cartApi.clearCart(cart.value.userId)
      await fetchCart()
    }
  } catch (error) {
    console.error('清空购物车失败:', error)
    alert('清空失败，请稍后重试')
  }
}

const checkout = () => {
  alert('结算功能开发中...')
  // router.push('/checkout')
}

const goToFoodList = () => {
  router.push('/')
}

onMounted(() => {
  fetchCart()
})
</script>

<style scoped>
.cart {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.cart-header h1 {
  color: #333;
  margin: 0;
}

.clear-btn {
  padding: 8px 16px;
  background-color: #fff;
  color: #ff6b6b;
  border: 1px solid #ff6b6b;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.clear-btn:hover {
  background-color: #ff6b6b;
  color: white;
}

.loading {
  text-align: center;
  padding: 50px;
  font-size: 18px;
  color: #666;
}

.cart-content {
  display: grid;
  grid-template-columns: 1fr 350px;
  gap: 30px;
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.cart-item {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr auto;
  gap: 20px;
  align-items: center;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.item-info h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 18px;
}

.item-price {
  color: #666;
  margin: 0;
}

.item-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.item-actions button {
  width: 32px;
  height: 32px;
  border: 1px solid #ddd;
  background-color: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 18px;
  transition: all 0.3s;
}

.item-actions button:hover:not(:disabled) {
  background-color: #ff6b6b;
  color: white;
  border-color: #ff6b6b;
}

.item-actions button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity {
  font-size: 16px;
  font-weight: 500;
  min-width: 30px;
  text-align: center;
}

.item-subtotal {
  text-align: right;
}

.item-subtotal p:first-child {
  color: #999;
  font-size: 14px;
  margin: 0 0 5px 0;
}

.subtotal-amount {
  color: #ff6b6b;
  font-size: 20px;
  font-weight: bold;
  margin: 0;
}

.remove-btn {
  padding: 8px 16px;
  background-color: #fff;
  color: #999;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.remove-btn:hover {
  color: #ff6b6b;
  border-color: #ff6b6b;
}

.cart-summary {
  background: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  height: fit-content;
  position: sticky;
  top: 20px;
}

.summary-content h2 {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  color: #666;
}

.summary-row.total {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 2px solid #eee;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.total-amount {
  color: #ff6b6b;
  font-size: 24px;
}

.checkout-btn {
  width: 100%;
  padding: 15px;
  margin-top: 20px;
  background-color: #ff6b6b;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 18px;
  font-weight: 500;
  transition: background-color 0.3s;
}

.checkout-btn:hover {
  background-color: #ff5252;
}

.empty-cart {
  text-align: center;
  padding: 100px 20px;
}

.empty-icon {
  font-size: 80px;
  margin-bottom: 20px;
}

.empty-cart p {
  color: #999;
  font-size: 20px;
  margin-bottom: 30px;
}

.go-shopping-btn {
  padding: 12px 40px;
  background-color: #ff6b6b;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.go-shopping-btn:hover {
  background-color: #ff5252;
}

@media (max-width: 968px) {
  .cart-content {
    grid-template-columns: 1fr;
  }

  .cart-item {
    grid-template-columns: 1fr;
    gap: 15px;
  }

  .item-subtotal {
    text-align: left;
  }

  .cart-summary {
    position: static;
  }
}
</style>
