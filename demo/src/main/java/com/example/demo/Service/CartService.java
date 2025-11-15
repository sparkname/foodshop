package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entitydto.Cart;
import com.example.demo.Entitydto.CartItem;
import com.example.demo.Entitydto.Food;
import com.example.demo.Entitydto.User;
import com.example.demo.Repository.CartItemRepository;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.FoodRepository;
import com.example.demo.Repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private FoodRepository foodRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 获取或创建用户购物车
     */
    public Cart getOrCreateCart(String username) {
        Optional<Cart> cartOpt = userRepository.findByUsername(username)
            .flatMap(user -> cartRepository.findByUser_Id(user.getId()));
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        }
        
        // 创建新购物车
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }
    
    /**
     * 添加商品到购物车
     */
    public Cart addItemToCart(String username, Long foodId, Integer quantity) {
        Cart cart = getOrCreateCart(username);
        
        // 检查商品是否存在且可用
        Food food = foodRepository.findById(foodId)
            .orElseThrow(() -> new RuntimeException("商品不存在"));
        
        if (!food.getAvailable()) {
            throw new RuntimeException("商品已下架");
        }
        
        if (food.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        
        // 检查购物车中是否已有该商品
        Optional<CartItem> existingItem = cartItemRepository
            .findByCartIdAndFoodId(cart.getId(), foodId);
        
        if (existingItem.isPresent()) {
            // 更新数量
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            if (food.getStock() < newQuantity) {
                throw new RuntimeException("库存不足");
            }
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            // 添加新商品
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setFood(food);
            newItem.setQuantity(quantity);
            newItem.setPrice(food.getPrice());
            cartItemRepository.save(newItem);
        }
        
        return cartRepository.findById(cart.getId()).get();
    }
    
    /**
     * 更新购物车商品数量
     */
    public Cart updateItemQuantity(String username, Long cartItemId, Integer quantity) {
        Cart cart = getOrCreateCart(username);
        
        CartItem item = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new RuntimeException("购物车商品不存在"));
        
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("无权操作此购物车商品");
        }
        
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            // 检查库存
            if (item.getFood().getStock() < quantity) {
                throw new RuntimeException("库存不足");
            }
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        
        return cartRepository.findById(cart.getId()).get();
    }
    
    /**
     * 从购物车删除商品
     */
    public Cart removeItemFromCart(String username, Long cartItemId) {
        Cart cart = getOrCreateCart(username);
        
        CartItem item = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new RuntimeException("购物车商品不存在"));
        
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("无权操作此购物车商品");
        }
        
        cartItemRepository.delete(item);
        return cartRepository.findById(cart.getId()).get();
    }
    
    /**
     * 清空购物车
     */
    public void clearCart(String username) {
        Cart cart = getOrCreateCart(username);
        cartItemRepository.deleteByCartId(cart.getId());
    }
    
    /**
     * 获取购物车商品列表
     */
    public List<CartItem> getCartItems(String username) {
        Cart cart = getOrCreateCart(username);
        return cartItemRepository.findByCartId(cart.getId());
    }
    
    /**
     * 计算购物车总价
     */
    public BigDecimal calculateCartTotal(String username) {
        List<CartItem> items = getCartItems(username);
        return items.stream()
            .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * 获取购物车
     */
    public Cart getCart(String username) {
        return getOrCreateCart(username);
    }
    
    /**
     * 检查购物车商品库存
     */
    public boolean checkCartItemsStock(String username) {
        List<CartItem> items = getCartItems(username);
        for (CartItem item : items) {
            if (item.getFood().getStock() < item.getQuantity()) {
                return false;
            }
            if (!item.getFood().getAvailable()) {
                return false;
            }
        }
        return true;
    }
}
