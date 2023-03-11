package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderDishService;
import com.waiter.waiter.services.OrderService;
import com.waiter.waiter.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

public class OrderDishController {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDishService orderDishService;
    @Autowired
    OrderService orderService;
    @Autowired
    RestaurantTableService restaurantTableService;
    @PostMapping("/add-dishes/{orderId}")
    private String addDishesInOrder(Integer orderId, Model model) {
        orderDishService.prepareToAddDishes(orderId,model);

        return "/orders/add-dish-to-order";
    }
    @PostMapping("/order-dish/add-to-order/submit/{orderId}")
    private String saveDishesToOrder(@PathVariable(name = "orderId") Integer orderId, OrderDishHelp orderDishHelp, Model model) {
        orderDishService.saveAddedDishes(orderId,orderDishHelp,model);
        int tId=restaurantTableService.getTableIdByOrderId(orderId);
        return "redirect:/orders/view/"+tId;
    }
}
