package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
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
    @Autowired
    RestaurantTableService restaurantTableService;
    @PostMapping("/add-dishes/{orderId}")
    private String addDishesInOrder(@PathVariable(name = "orderId") Integer orderId, Model model) {
        orderDishService.prepareToAddDishes(orderId,model);

        return "/orders/add-dish-to-order";
    }
    @PostMapping("/add-to-order/submit/{orderId}")
    private String saveDishesToOrder(@PathVariable(name = "orderId") Integer orderId, OrderDishHelp orderDishHelp, Model model) {
        orderDishService.saveAddedDishes(orderId,orderDishHelp,model);
        int tId=restaurantTableService.getTableIdByOrderId(orderId);
        return "redirect:/orders/view/"+tId;
    }
    @PostMapping("/delete-dishes/{orderDishId}")
    private String deleteDishesInOrder(@PathVariable(name = "orderDishId") Integer orderDishId, Model model) {
        int orderId = orderDishService.findOrderIdByOrderDishId(orderDishId);
        orderDishService.deleteOrderDishById(orderDishId,orderId);
        int tId=restaurantTableService.getTableIdByOrderId(orderId);
        return "redirect:/orders/view/"+tId;
    }

    @PostMapping("/add-one-to-quantity/{orderDishId}")
    private String addOneToQuantity(@PathVariable(name = "orderDishId") Integer orderDishId, Model model) {
        int orderId = orderDishService.findOrderIdByOrderDishId(orderDishId);
        orderDishService.addOneToQuantity(orderDishId);
        int tId=restaurantTableService.getTableIdByOrderId(orderId);
        return "redirect:/orders/view/"+tId;
    }
    @PostMapping("/remove-one-from-quantity/{orderDishId}")
    private String removeOneFromQuantity(@PathVariable(name = "orderDishId") Integer orderDishId, Model model) {
        int orderId = orderDishService.findOrderIdByOrderDishId(orderDishId);
        orderDishService.removeOneFromQuantity(orderDishId);
        int tId=restaurantTableService.getTableIdByOrderId(orderId);
        return "redirect:/orders/view/"+tId;
    }
}
