package com.waiter.waiter.controllers;

import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.helpingClasses.OrderDrinkHelp;
import com.waiter.waiter.services.OrderDishService;
import com.waiter.waiter.services.OrderDrinkService;
import com.waiter.waiter.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order-drink")
public class OrderDrinkController {
    @Autowired
    OrderDrinkService orderDrinkService;
    @Autowired
    RestaurantTableService restaurantTableService;
    @PostMapping("/add-drinks/{orderId}")
    private String addDrinksInOrder(@PathVariable(name = "orderId") Integer orderId, Model model) {
        orderDrinkService.prepareToAddDrinks(orderId,model);
        return "/orders/add-drink-to-order";
    }
    @PostMapping("/add-to-order/submit/{orderId}")
    private String saveDrinksToOrder(@PathVariable(name = "orderId") Integer orderId, OrderDrinkHelp orderDrinkHelp, Model model) {
        orderDrinkService.saveAddedDrinks(orderId,orderDrinkHelp,model);
        int tId=restaurantTableService.getTableIdByOrderId(orderId);
        return "redirect:/orders/view/"+tId;
    }
    @PostMapping("/delete-drinks/{orderDrinkId}")
    private String deleteDrinksInOrder(@PathVariable(name = "orderDrinkId") Integer orderDrinkId, Model model) {
        int orderId = orderDrinkService.findOrderIdByOrderDrinkId(orderDrinkId);
        orderDrinkService.deleteOrderDrinkById(orderDrinkId,orderId);
        int tId=restaurantTableService.getTableIdByOrderId(orderId);
        return "redirect:/orders/view/"+tId;
    }

    @PostMapping("/add-one-to-quantity/{orderDrinkId}")
    private String addOneToQuantity(@PathVariable(name = "orderDrinkId") Integer orderDrinkId, Model model) {
        int orderId = orderDrinkService.findOrderIdByOrderDrinkId(orderDrinkId);
        orderDrinkService.addOneToQuantity(orderDrinkId);
        int tId=restaurantTableService.getTableIdByOrderId(orderId);
        return "redirect:/orders/view/"+tId;
    }
    @PostMapping("/remove-one-from-quantity/{orderDrinkId}")
    private String removeOneFromQuantity(@PathVariable(name = "orderDrinkId") Integer orderDrinkId, Model model) {
        int orderId = orderDrinkService.findOrderIdByOrderDrinkId(orderDrinkId);
        orderDrinkService.removeOneFromQuantity(orderDrinkId);
        int tId=restaurantTableService.getTableIdByOrderId(orderId);
        return "redirect:/orders/view/"+tId;
    }
}
