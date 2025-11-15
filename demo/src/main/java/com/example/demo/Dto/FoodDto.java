package com.example.demo.Dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FoodDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String imageUrl;
    private Boolean available;
}