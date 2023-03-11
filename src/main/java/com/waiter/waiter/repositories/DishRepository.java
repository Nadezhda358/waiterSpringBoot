package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Dish;
import org.springframework.data.repository.CrudRepository;

public interface DishRepository extends CrudRepository<Dish, Integer> {
}
