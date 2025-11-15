package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entitydto.Food;
import com.example.demo.Repository.FoodRepository;

import java.util.List;

@Service
@Transactional
public class FoodService {
    
    @Autowired
    private FoodRepository foodRepository;
    
    /**
     * 获取所有商品
     */
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
    
    /**
     * 获取所有可用商品
     */
    public List<Food> getAvailableFoods() {
        return foodRepository.findByAvailableTrue();
    }
    
    /**
     * 根据ID获取商品
     */
    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("商品不存在"));
    }
    
    /**
     * 根据分类ID获取商品
     */
    public List<Food> getFoodsByCategory(Long categoryId) {
        // 暂时返回所有可用商品，因为 category 是字符串类型
        return foodRepository.findByAvailableTrue();
    }
    
    /**
     * 根据名称搜索商品
     */
    public List<Food> searchFoodsByName(String name) {
        return foodRepository.findByNameContaining(name);
    }
    
    /**
     * 添加新商品
     */
    public Food addFood(Food food) {
        if (food.getAvailable() == null) {
            food.setAvailable(true);
        }
        return foodRepository.save(food);
    }
    
    /**
     * 更新商品信息
     */
    public Food updateFood(Long id, Food foodDetails) {
        Food food = getFoodById(id);
        
        if (foodDetails.getName() != null) {
            food.setName(foodDetails.getName());
        }
        if (foodDetails.getDescription() != null) {
            food.setDescription(foodDetails.getDescription());
        }
        if (foodDetails.getPrice() != null) {
            food.setPrice(foodDetails.getPrice());
        }
        if (foodDetails.getStock() != null) {
            food.setStock(foodDetails.getStock());
        }
        if (foodDetails.getCategory() != null) {
            food.setCategory(foodDetails.getCategory());
        }
        if (foodDetails.getImageUrl() != null) {
            food.setImageUrl(foodDetails.getImageUrl());
        }
        if (foodDetails.getAvailable() != null) {
            food.setAvailable(foodDetails.getAvailable());
        }
        
        return foodRepository.save(food);
    }
    
    /**
     * 删除商品
     */
    public void deleteFood(Long id) {
        Food food = getFoodById(id);
        foodRepository.delete(food);
    }
    
    /**
     * 更新商品库存
     */
    public Food updateStock(Long id, Integer stock) {
        Food food = getFoodById(id);
        food.setStock(stock);
        return foodRepository.save(food);
    }
    
    /**
     * 减少商品库存
     */
    public Food decreaseStock(Long id, Integer quantity) {
        Food food = getFoodById(id);
        if (food.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        food.setStock(food.getStock() - quantity);
        return foodRepository.save(food);
    }
    
    /**
     * 增加商品库存
     */
    public Food increaseStock(Long id, Integer quantity) {
        Food food = getFoodById(id);
        food.setStock(food.getStock() + quantity);
        return foodRepository.save(food);
    }
    
    /**
     * 上架商品
     */
    public Food enableFood(Long id) {
        Food food = getFoodById(id);
        food.setAvailable(true);
        return foodRepository.save(food);
    }
    
    /**
     * 下架商品
     */
    public Food disableFood(Long id) {
        Food food = getFoodById(id);
        food.setAvailable(false);
        return foodRepository.save(food);
    }
}
