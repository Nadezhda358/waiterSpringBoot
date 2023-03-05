package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import com.waiter.waiter.services.OrderService;
import com.waiter.waiter.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    RestaurantTablesRepository restaurantTablesRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    RestaurantTableService restaurantTableService;
    @PostMapping("/create/{tId}")
    private String createOrder(@PathVariable(name="tId") Integer tId, Model model) {
        Order order=new Order();
        RestaurantTable table = restaurantTableService.getTableById(tId);
        table.setHasOrder(true);
        restaurantTablesRepository.save(table);
        order.setTable(table);
        order.setCreatedOn(LocalDateTime.now());
        orderRepository.save(order);
        //return "/orders/add-order";
        model.addAttribute(order);
        return "/orders/view-order";
    }

    @PostMapping("/view/{tId}")
    private String viewOrderByTableId(@PathVariable(name="tId") Integer tId, Model model){
        Optional<RestaurantTable> t=restaurantTablesRepository.findById(tId);
        Order order=orderService.getOrderByTableId(t);
        model.addAttribute(order);
        return "/orders/view-order";
    }


}