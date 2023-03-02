package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Drink;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrinkRepository extends CrudRepository<Drink, Integer> {
    @Query("SELECT u FROM Drink u WHERE u.drinkType = :drinkType")
    List<Dish> getDrinksByType(@Param("drinkType") String drinkType);
}
