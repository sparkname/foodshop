package com.example.demo.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entitydto.Food;

import java.util.List;
@Repository

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByCategory(String category);
    List<Food> findByAvailableTrue();
    List<Food> findByNameContaining(String name);

}
