package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.*;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.repositories.*;
import com.waiter.waiter.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PostMapping("/create/{tId}")
    private String createOrder(@PathVariable(name = "tId") Integer tId, Model model) {
       orderService.createOrder(tId);
        return "redirect:/orders/view/"+tId;
    }
    @PostMapping("/view/{tId}")
    private String viewOrderByTableId(@PathVariable(name = "tId") Integer tId, Model model) {
        orderService.viewOrderByTableId(tId, model);
        return "/orders/view-order";
    }
    @GetMapping("/view/{tId}")
    private String viewOrderByTableId1(@PathVariable(name = "tId") Integer tId, Model model) {
        orderService.viewOrderByTableId(tId, model);
        return "/orders/view-order";
    }
    @PostMapping("/change-status/{orderId}")
    public String changeStatus(@PathVariable(name = "orderId") Integer orderId, Model model) {
        Order order=orderService.getOrderById(orderId);
        orderService.ChangeStatus(order);
        if(order.getOrderStatus()==OrderStatus.PAID){
            return "redirect:/tables";
        }
        int tId=order.getTable().getId();
        return "redirect:/orders/view/"+tId;
    }
}
