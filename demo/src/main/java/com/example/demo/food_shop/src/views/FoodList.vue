<template>
  <div class="food-list">
    <div class="header">
      <h1>美食广场</h1>
      <div class="search-bar">
        <input
          v-model="searchKeyword"
          @keyup.enter="handleSearch"
          placeholder="搜索美食..."
          type="text"
        />
        <button @click="handleSearch">搜索</button>
      </div>
    </div>

    <div class="categories">
      <button
        v-for="cat in categories"
        :key="cat"
        :class="{ active: selectedCategory === cat }"
        @click="selectCategory(cat)"
      >
        {{ cat }}
      </button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else class="food-grid">
      <div v-for="food in foods" :key="food.id" class="food-card">
        <div class="food-image">
          <img :src="food.imageUrl || '/placeholder.jpg'" :alt="food.name" />
          <span v-if="!food.available || food.stock === 0" class="sold-out">已售罄</span>
        </div>
        <div class="food-info">
          <h3>{{ food.name }}</h3>
          <p class="description">{{ food.description }}</p>
          <div class="food-footer">
            <div class="price">¥{{ food.price.toFixed(2) }}</div>
            <div class="stock">库存: {{ food.stock }}</div>
          </div>
          <button
            class="add-btn"
            @click="addToCart(food)"
            :disabled="!food.available || food.stock === 0"
          >
            {{ food.available && food.stock > 0 ? '加入购物车' : '已售罄' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="!loading && foods.length === 0" class="empty">
      <p>暂无商品</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { foodApi } from '@/api/food'
import { cartApi } from '@/api/cart'
import type { FoodDto } from '@/types'

const foods = ref<FoodDto[]>([])
const loading = ref(false)
const searchKeyword = ref('')
const selectedCategory = ref('全部')
const categories = ref(['全部', '主食', '小吃', '饮料', '甜点'])

const fetchFoods = async () => {
  loading.value = true
  try {
    const params: any = {}
    if (selectedCategory.value !== '全部') {
      params.category = selectedCategory.value
    }
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    foods.value = await foodApi.getFoods(params)
  } catch (error) {
    console.error('获取食品列表失败:', error)
    alert('获取食品列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  fetchFoods()
}

const selectCategory = (category: string) => {
  selectedCategory.value = category
  fetchFoods()
}

const addToCart = async (food: FoodDto) => {
  try {
    await cartApi.addToCart({
      foodId: food.id,
      quantity: 1
    })
    alert(`${food.name} 已添加到购物车`)
  } catch (error) {
    console.error('添加到购物车失败:', error)
    alert('添加失败，请稍后重试')
  }
}

onMounted(() => {
  fetchFoods()
})
</script>

<style scoped>
.food-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h1 {
  color: #333;
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.search-bar input {
  width: 300px;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.search-bar button {
  padding: 10px 20px;
  background-color: #ff6b6b;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-bar button:hover {
  background-color: #ff5252;
}

.categories {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.categories button {
  padding: 8px 20px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.categories button:hover {
  background-color: #e0e0e0;
}

.categories button.active {
  background-color: #ff6b6b;
  color: white;
  border-color: #ff6b6b;
}

.loading {
  text-align: center;
  padding: 50px;
  font-size: 18px;
  color: #666;
}

.food-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 25px;
}

.food-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.food-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.food-image {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.food-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.sold-out {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 10px 20px;
  border-radius: 4px;
  font-size: 18px;
  font-weight: bold;
}

.food-info {
  padding: 15px;
}

.food-info h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 18px;
}

.description {
  color: #666;
  font-size: 14px;
  margin-bottom: 15px;
  line-height: 1.5;
  height: 42px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.food-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.price {
  color: #ff6b6b;
  font-size: 24px;
  font-weight: bold;
}

.stock {
  color: #999;
  font-size: 14px;
}

.add-btn {
  width: 100%;
  padding: 10px;
  background-color: #ff6b6b;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: background-color 0.3s;
}

.add-btn:hover:not(:disabled) {
  background-color: #ff5252;
}

.add-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.empty {
  text-align: center;
  padding: 50px;
  color: #999;
  font-size: 18px;
}
</style>
