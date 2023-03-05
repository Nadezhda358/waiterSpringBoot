package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {}
