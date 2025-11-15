-- 创建数据库(如果不存在)
CREATE DATABASE IF NOT EXISTS programe1db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE programe1db;

-- 删除已有表(按外键依赖顺序)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS food;
DROP TABLE IF EXISTS user;
SET FOREIGN_KEY_CHECKS = 1;

-- 创建用户表
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(200),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建商品表
CREATE TABLE food (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50),
    image_url VARCHAR(255),
    stock INT DEFAULT 0,
    available BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建购物车表
CREATE TABLE cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建购物车商品表
CREATE TABLE cart_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cart_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    price DECIMAL(10, 2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES food(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建订单表
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    delivery_address VARCHAR(255),
    contact_phone VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建订单商品表
CREATE TABLE order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES food(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert user data
INSERT INTO user (id, username, password, email, phone, address) VALUES
(1, 'admin', '123456', 'admin@foodshop.com', '13800138000', 'Beijing'),
(2, 'user01', '123456', 'user01@example.com', '13800138001', 'Shanghai'),
(3, 'user02', '123456', 'user02@example.com', '13800138002', 'Guangzhou'),
(4, 'user03', '123456', 'user03@example.com', '13800138003', 'Shenzhen'),
(5, 'user04', '123456', 'user04@example.com', '13800138004', 'Hangzhou');

-- Insert food data
INSERT INTO food (id, name, description, price, category, image_url, stock, available) VALUES
-- Main dishes
(1, 'Braised Chicken Rice', 'Classic braised chicken with rice', 28.00, 'Main', 'https://example.com/food1.jpg', 100, true),
(2, 'Spicy Hot Pot', 'Spicy and fresh ingredients', 35.00, 'Main', 'https://example.com/food2.jpg', 80, true),
(3, 'Shaxian Snack Set', 'Classic Shaxian combo', 25.00, 'Main', 'https://example.com/food3.jpg', 120, true),
(4, 'Lanzhou Ramen', 'Authentic beef noodles', 22.00, 'Main', 'https://example.com/food4.jpg', 90, true),
(5, 'Rice Bowl', 'Egg and tomato rice bowl', 20.00, 'Main', 'https://example.com/food5.jpg', 150, true),

-- Snacks
(6, 'Grilled Cold Noodle', 'Northeast specialty', 12.00, 'Snack', 'https://example.com/food6.jpg', 200, true),
(7, 'Pancake', 'Tianjin traditional breakfast', 10.00, 'Snack', 'https://example.com/food7.jpg', 180, true),
(8, 'Roujiamo', 'Shaanxi specialty', 15.00, 'Snack', 'https://example.com/food8.jpg', 160, true),
(9, 'Fried Chicken Burger', 'Crispy chicken burger', 25.00, 'Snack', 'https://example.com/food9.jpg', 100, true),
(10, 'Takoyaki', 'Japanese street food', 18.00, 'Snack', 'https://example.com/food10.jpg', 140, true),

-- Drinks
(11, 'Bubble Tea', 'Classic bubble milk tea', 12.00, 'Drink', 'https://example.com/food11.jpg', 300, true),
(12, 'Lemon Tea', 'Refreshing lemon tea', 10.00, 'Drink', 'https://example.com/food12.jpg', 250, true),
(13, 'Coke', 'Iced cola', 5.00, 'Drink', 'https://example.com/food13.jpg', 500, true),
(14, 'Fresh Juice', 'Seasonal fruit juice', 15.00, 'Drink', 'https://example.com/food14.jpg', 200, true),
(15, 'Soy Milk', 'Fresh ground soy milk', 6.00, 'Drink', 'https://example.com/food15.jpg', 400, true),

-- Desserts
(16, 'Mango Pancake', 'Fresh mango dessert', 22.00, 'Dessert', 'https://example.com/food16.jpg', 80, true),
(17, 'Double Skin Milk', 'Shunde specialty dessert', 18.00, 'Dessert', 'https://example.com/food17.jpg', 100, true),
(18, 'Tiramisu', 'Italian classic dessert', 28.00, 'Dessert', 'https://example.com/food18.jpg', 60, true),
(19, 'Egg Tart', 'Portuguese egg tart', 8.00, 'Dessert', 'https://example.com/food19.jpg', 200, true),
(20, 'Ice Cream', 'Various flavors', 12.00, 'Dessert', 'https://example.com/food20.jpg', 150, true);

-- Insert cart
INSERT INTO cart (id, user_id) VALUES
(1, 2),
(2, 3),
(3, 4);

-- Insert cart items
INSERT INTO cart_item (id, cart_id, food_id, quantity, price) VALUES
(1, 1, 1, 2, 28.00),
(2, 1, 11, 1, 12.00),
(3, 1, 6, 1, 12.00),
(4, 2, 2, 1, 35.00),
(5, 2, 14, 2, 15.00),
(6, 3, 9, 1, 25.00),
(7, 3, 16, 2, 22.00),
(8, 3, 13, 1, 5.00);

-- Insert orders
INSERT INTO orders (id, user_id, total_amount, status, delivery_address, contact_phone, created_at) VALUES
(1, 2, 68.00, 'COMPLETED', 'Shanghai Pudong District', '13800138001', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2, 3, 95.00, 'SHIPPED', 'Guangzhou Tianhe District', '13800138002', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 4, 150.00, 'PAID', 'Shenzhen Nanshan District', '13800138003', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 5, 45.00, 'PENDING', 'Hangzhou Xihu District', '13800138004', NOW());

-- Insert order items
INSERT INTO order_item (id, order_id, food_id, quantity, price, subtotal) VALUES
(1, 1, 1, 2, 28.00, 56.00),
(2, 1, 11, 1, 12.00, 12.00),
(3, 2, 2, 1, 35.00, 35.00),
(4, 2, 3, 2, 25.00, 50.00),
(5, 2, 12, 1, 10.00, 10.00),
(6, 3, 9, 2, 25.00, 50.00),
(7, 3, 16, 3, 22.00, 66.00),
(8, 3, 18, 1, 28.00, 28.00),
(9, 3, 13, 2, 5.00, 10.00),
(10, 4, 4, 1, 22.00, 22.00),
(11, 4, 7, 2, 10.00, 20.00),
(12, 4, 15, 1, 6.00, 6.00);

-- View import results
SELECT 'Users' as 'Table', COUNT(*) as 'Count' FROM user
UNION ALL SELECT 'Foods', COUNT(*) FROM food
UNION ALL SELECT 'Carts', COUNT(*) FROM cart
UNION ALL SELECT 'Cart Items', COUNT(*) FROM cart_item
UNION ALL SELECT 'Orders', COUNT(*) FROM orders
UNION ALL SELECT 'Order Items', COUNT(*) FROM order_item;