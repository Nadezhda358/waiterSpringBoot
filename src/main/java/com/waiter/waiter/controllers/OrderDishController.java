package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderDishService;
import com.waiter.waiter.services.OrderService;
import com.waiter.waiter.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
@Controller
@RequestMapping("/order-dish")
public class OrderDishController {
    @Autowired
    OrderDishService orderDishService;
    @PostMapping("/add-dishes/{orderId}")
    private String addDishesInOrder(Integer orderId, Model model) {
        orderDishService.prepareToAddDishes(orderId,model);

        return "/orders/add-dish-to-order";
    }
    @PostMapping("/order-dish/add-to-order/submit/{orderId}")
    private String saveDishesToOrder(@PathVariable(name = "orderId") Integer orderId, OrderDishHelp orderDishHelp, Model model) {
        orderDishService.saveAddedDishes(orderId,orderDishHelp,model);
        int tId=orderDishService.getTableIdByOrderId(orderId);
        return "redirect:/orders/view/"+tId;
    }
}
