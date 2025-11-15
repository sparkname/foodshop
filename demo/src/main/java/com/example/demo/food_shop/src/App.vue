<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import { computed } from 'vue'

const route = useRoute()
const cartCount = computed(() => 0) // 可以从 store 获取购物车数量
</script>

<template>
  <div id="app">
    <header class="app-header">
      <div class="header-content">
        <div class="logo">
          <router-link to="/">🍔 美食商城</router-link>
        </div>
        <nav class="nav-menu">
          <router-link to="/" :class="{ active: route.path === '/' }">
            首页
          </router-link>
          <router-link to="/cart" :class="{ active: route.path === '/cart' }">
            购物车
            <span v-if="cartCount > 0" class="badge">{{ cartCount }}</span>
          </router-link>
          <router-link to="/profile" :class="{ active: route.path === '/profile' }">
            个人中心
          </router-link>
        </nav>
      </div>
    </header>

    <main class="app-main">
      <RouterView />
    </main>

    <footer class="app-footer">
      <p>&copy; 2025 美食商城. All rights reserved.</p>
    </footer>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial,
    sans-serif;
  background-color: #f5f5f5;
  color: #333;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 64px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo a {
  font-size: 24px;
  font-weight: bold;
  color: #ff6b6b;
  text-decoration: none;
  transition: color 0.3s;
}

.logo a:hover {
  color: #ff5252;
}

.nav-menu {
  display: flex;
  gap: 30px;
}

.nav-menu a {
  color: #666;
  text-decoration: none;
  font-size: 16px;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s;
  position: relative;
}

.nav-menu a:hover {
  color: #ff6b6b;
  background-color: #fff5f5;
}

.nav-menu a.active {
  color: #ff6b6b;
  font-weight: 500;
}

.badge {
  position: absolute;
  top: 0;
  right: 4px;
  background-color: #ff6b6b;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 18px;
  text-align: center;
}

.app-main {
  flex: 1;
  padding: 20px 0;
}

.app-footer {
  background-color: #333;
  color: white;
  text-align: center;
  padding: 20px;
  margin-top: auto;
}

.app-footer p {
  font-size: 14px;
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 10px;
  }

  .logo a {
    font-size: 20px;
  }

  .nav-menu {
    gap: 15px;
  }

  .nav-menu a {
    font-size: 14px;
    padding: 6px 12px;
  }
}
</style>
