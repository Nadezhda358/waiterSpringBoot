package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DishRepository extends CrudRepository<Dish, Integer> {
    @Query("SELECT u FROM Dish u WHERE u.dishType = :dishType")
    List<Dish> getDishesByType(@Param("dishType") String dishType);

    //List<Dish> getDishes()
}
