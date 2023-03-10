package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Drink;
import org.springframework.data.repository.CrudRepository;


public interface DrinkRepository extends CrudRepository<Drink, Integer> {}
