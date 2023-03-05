package com.waiter.waiter.services;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public Order getOrderByTableId(Optional<RestaurantTable> tId){
        Order order = orderRepository.getOrderByTableId(tId);
            return order;
    }
}
