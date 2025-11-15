package com.example.demo.Entitydto;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    //那这个为什么不直接定义private int user_i呢
    /*因为我们希望在Java代码中直接操作User对象，而不是仅仅操作一个ID。
    个人理解
    像这一些直接个你在写在mysql上面写代码建表是一样的，建表的时候要定义一些数据的依赖关系
    在数据库中定义的关系也要在在这一些实体类中写好那一些对应关系，不是你在数据库中定义了关系
    在实体类中就可以不定义关系，这样会导致数据不一致的问题
    */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
     
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}
