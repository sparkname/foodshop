package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entitydto.Cart;
import com.example.demo.Entitydto.CartItem;
import com.example.demo.Entitydto.Order;
import com.example.demo.Entitydto.OrderItem;
import com.example.demo.Entitydto.User;
import com.example.demo.Repository.CartItemRepository;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.OrderItemRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FoodService foodService;
    
    @Autowired
    private CartService cartService;
    
    /**
     * 从购物车创建订单
     */
    public Order createOrderFromCart(Long userId, String deliveryAddress, String contactPhone, String remark) {
        // 获取用户
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 获取购物车
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("购物车为空"));
        
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车中没有商品");
        }
        
        // 检查库存
        if (!cartService.checkCartItemsStock(userId)) {
            throw new RuntimeException("部分商品库存不足或已下架");
        }
        
        // 创建订单
        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setDeliveryAddress(deliveryAddress);
        order.setContactPhone(contactPhone);
        order.setRemark(remark);
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // 保存订单以获取ID
        order = orderRepository.save(order);
        
        // 创建订单项并扣减库存
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            
            BigDecimal subtotal = cartItem.getPrice()
                .multiply(new BigDecimal(cartItem.getQuantity()));
            orderItem.setSubtotal(subtotal);
            
            totalAmount = totalAmount.add(subtotal);
            
            orderItemRepository.save(orderItem);
            
            // 扣减库存
            foodService.decreaseStock(cartItem.getFood().getId(), cartItem.getQuantity());
        }
        
        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);
        
        // 清空购物车
        cartService.clearCart(userId);
        
        return order;
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * 获取订单详情
     */
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("订单不存在"));
    }
    
    /**
     * 获取用户所有订单
     */
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    
    /**
     * 根据状态查询订单
     */
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    /**
     * 获取所有订单
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    /**
     * 支付订单
     */
    public Order payOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new RuntimeException("订单状态不正确");
        }
        
        order.setStatus(Order.OrderStatus.PAID);
        return orderRepository.save(order);
    }
    
    /**
     * 发货
     */
    public Order shipOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        if (order.getStatus() != Order.OrderStatus.PAID) {
            throw new RuntimeException("订单未支付");
        }
        
        order.setStatus(Order.OrderStatus.SHIPPED);
        return orderRepository.save(order);
    }
    
    /**
     * 确认收货
     */
    public Order deliverOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        if (order.getStatus() != Order.OrderStatus.SHIPPED) {
            throw new RuntimeException("订单未发货");
        }
        
        order.setStatus(Order.OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }
    
    /**
     * 取消订单
     */
    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("订单已完成,无法取消");
        }
        
        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new RuntimeException("订单已取消");
        }
        
        // 恢复库存
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : orderItems) {
            foodService.increaseStock(item.getFood().getId(), item.getQuantity());
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
    
    /**
     * 获取订单项
     */
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
}
