package com.example.demo.Dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long id;
    private Long foodId;
    private String foodName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}