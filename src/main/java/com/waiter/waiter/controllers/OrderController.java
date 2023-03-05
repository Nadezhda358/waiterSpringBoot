package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderService;
import com.waiter.waiter.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    RestaurantTableService restaurantTableService;
    @PostMapping("/create/{tId}")
    private String moreInfo(@PathVariable(name="tId") Integer tId, Model model) {
        Order order=new Order();
        order.setTable(restaurantTableService.getTableById(tId));
        orderRepository.save(order);
        return "/orders/add-order";
    }

}
