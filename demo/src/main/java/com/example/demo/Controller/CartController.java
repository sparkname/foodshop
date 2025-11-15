package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entitydto.Cart;
import com.example.demo.Entitydto.CartItem;
import com.example.demo.Service.CartService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * 获取购物车
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        try {
            Cart cart = cartService.getCart(userId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取购物车商品列表
     */
    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long userId) {
        try {
            List<CartItem> items = cartService.getCartItems(userId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 添加商品到购物车
     */
    @PostMapping("/{userId}/items")
    public ResponseEntity<?> addItemToCart(
            @PathVariable Long userId,
            @RequestParam Long foodId,
            @RequestParam Integer quantity) {
        try {
            Cart cart = cartService.addItemToCart(userId, foodId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    /**
     * 更新购物车商品数量
     */
    @PutMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<?> updateItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        try {
            Cart cart = cartService.updateItemQuantity(userId, cartItemId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    /**
     * 从购物车删除商品
     */
    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<?> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Long cartItemId) {
        try {
            Cart cart = cartService.removeItemFromCart(userId, cartItemId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    /**
     * 清空购物车
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 计算购物车总价
     */
    @GetMapping("/{userId}/total")
    public ResponseEntity<BigDecimal> calculateCartTotal(@PathVariable Long userId) {
        try {
            BigDecimal total = cartService.calculateCartTotal(userId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 检查购物车库存
     */
    @GetMapping("/{userId}/check-stock")
    public ResponseEntity<Boolean> checkCartItemsStock(@PathVariable Long userId) {
        try {
            boolean hasStock = cartService.checkCartItemsStock(userId);
            return ResponseEntity.ok(hasStock);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
