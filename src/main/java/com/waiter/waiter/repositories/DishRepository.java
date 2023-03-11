package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Dish;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DishRepository extends CrudRepository<Dish, Integer> {
}
