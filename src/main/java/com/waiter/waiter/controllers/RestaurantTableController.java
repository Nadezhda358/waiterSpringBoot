package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.services.RestaurantTableService;
import com.waiter.waiter.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Table;
import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("/tables")
public class RestaurantTableController {
    @Autowired
    RestaurantTableService restaurantTableService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    //@GetMapping
    //private String getTables(Model model){
    //    model.addAttribute("restaurantTables",restaurantTableService.getAllTables());
    //    return "/orders/restaurant-tables";
    //}

    @GetMapping("/create")
    private String createDish(){
        restaurantTableService.createNewTable();
        return "redirect:/tables";
    }
    @GetMapping
    public String showTables(Model model, @RequestParam(required = false, defaultValue = "all") String filter) {
        //Iterable<RestaurantTable> tables = new ArrayList<>();
        //if (filter.equals("all")) {
        //    tables = restaurantTableRepository.findAll();
        //} else if (filter.equals("your")) {
        //    tables = orderRepository.getWaiterTables(userDetailsService.getLoggedUser().getId());
        //}
        model.addAttribute("loggedUser", userDetailsService.getLoggedUser());
        model.addAttribute("restaurantTables", restaurantTableService.getTables(filter));
        model.addAttribute("filter", filter);
        return "/orders/restaurant-tables";
    }

}
