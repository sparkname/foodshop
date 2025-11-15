package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entitydto.Food;
import com.example.demo.Service.FoodService;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@CrossOrigin(origins = "*")
public class FoodController {
    
    @Autowired
    private FoodService foodService;
    
    /**
     * 获取所有商品
     */
    @GetMapping
    public ResponseEntity<List<Food>> getAllFoods() {
        try {
            List<Food> foods = foodService.getAllFoods();
            return ResponseEntity.ok(foods);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取所有可用商品
     */
    @GetMapping("/available")
    public ResponseEntity<List<Food>> getAvailableFoods() {
        try {
            List<Food> foods = foodService.getAvailableFoods();
            return ResponseEntity.ok(foods);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据ID获取商品
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodById(@PathVariable Long id) {
        try {
            Food food = foodService.getFoodById(id);
            return ResponseEntity.ok(food);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    /**
     * 根据分类获取商品
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Food>> getFoodsByCategory(@PathVariable Long categoryId) {
        try {
            List<Food> foods = foodService.getFoodsByCategory(categoryId);
            return ResponseEntity.ok(foods);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFoods(@RequestParam String name) {
        try {
            List<Food> foods = foodService.searchFoodsByName(name);
            return ResponseEntity.ok(foods);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 添加商品
     */
    @PostMapping
    public ResponseEntity<?> addFood(@RequestBody Food food) {
        try {
            Food savedFood = foodService.addFood(food);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFood);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    /**
     * 更新商品
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFood(@PathVariable Long id, @RequestBody Food food) {
        try {
            Food updatedFood = foodService.updateFood(id, food);
            return ResponseEntity.ok(updatedFood);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFood(@PathVariable Long id) {
        try {
            foodService.deleteFood(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    /**
     * 更新库存
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        try {
            Food food = foodService.updateStock(id, stock);
            return ResponseEntity.ok(food);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    /**
     * 上架商品
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<?> enableFood(@PathVariable Long id) {
        try {
            Food food = foodService.enableFood(id);
            return ResponseEntity.ok(food);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    /**
     * 下架商品
     */
    @PatchMapping("/{id}/disable")
    public ResponseEntity<?> disableFood(@PathVariable Long id) {
        try {
            Food food = foodService.disableFood(id);
            return ResponseEntity.ok(food);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
