<template>
  <div class="user-profile">
    <div class="profile-header">
      <h1>个人信息</h1>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else class="profile-content">
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
            id="username"
            v-model="formData.username"
            type="text"
            placeholder="请输入用户名"
            required
          />
        </div>

        <div class="form-group">
          <label for="realName">真实姓名</label>
          <input
            id="realName"
            v-model="formData.realName"
            type="text"
            placeholder="请输入真实姓名"
            required
          />
        </div>

        <div class="form-group">
          <label for="email">邮箱</label>
          <input
            id="email"
            v-model="formData.email"
            type="email"
            placeholder="请输入邮箱"
            required
          />
        </div>

        <div class="form-group">
          <label for="phone">手机号</label>
          <input
            id="phone"
            v-model="formData.phone"
            type="tel"
            placeholder="请输入手机号"
            required
          />
        </div>

        <div class="form-group">
          <label for="address">地址</label>
          <textarea
            id="address"
            v-model="formData.address"
            placeholder="请输入地址"
            rows="4"
          ></textarea>
        </div>

        <div class="form-actions">
          <button type="button" @click="resetForm" class="reset-btn">重置</button>
          <button type="submit" class="submit-btn" :disabled="saving">
            {{ saving ? '保存中...' : '保存' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { userApi } from '@/api/user'
import type { UserDto } from '@/types'

const loading = ref(false)
const saving = ref(false)
const originalData = ref<UserDto | null>(null)

const formData = reactive({
  id: 0,
  username: '',
  realName: '',
  email: '',
  phone: '',
  address: ''
})

const fetchUserInfo = async () => {
  loading.value = true
  try {
    // 使用当前用户信息，或者使用固定 userId
    const user = await userApi.getCurrentUser()
    originalData.value = user
    Object.assign(formData, user)
  } catch (error) {
    console.error('获取用户信息失败:', error)
    alert('获取用户信息失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const handleSubmit = async () => {
  saving.value = true
  try {
    await userApi.updateUser(formData.id, formData)
    alert('保存成功')
    originalData.value = { ...formData }
  } catch (error) {
    console.error('保存失败:', error)
    alert('保存失败，请稍后重试')
  } finally {
    saving.value = false
  }
}

const resetForm = () => {
  if (originalData.value) {
    Object.assign(formData, originalData.value)
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.user-profile {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.profile-header {
  margin-bottom: 30px;
}

.profile-header h1 {
  color: #333;
  margin: 0;
}

.loading {
  text-align: center;
  padding: 50px;
  font-size: 18px;
  color: #666;
}

.profile-content {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

form {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  color: #333;
  font-weight: 500;
  font-size: 14px;
}

.form-group input,
.form-group textarea {
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #ff6b6b;
}

.form-group textarea {
  resize: vertical;
  font-family: inherit;
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: flex-end;
  margin-top: 10px;
}

.reset-btn,
.submit-btn {
  padding: 12px 30px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s;
}

.reset-btn {
  background-color: #f5f5f5;
  color: #666;
}

.reset-btn:hover {
  background-color: #e0e0e0;
}

.submit-btn {
  background-color: #ff6b6b;
  color: white;
}

.submit-btn:hover:not(:disabled) {
  background-color: #ff5252;
}

.submit-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .profile-content {
    padding: 20px;
  }

  .form-actions {
    flex-direction: column;
  }

  .reset-btn,
  .submit-btn {
    width: 100%;
  }
}
</style>
