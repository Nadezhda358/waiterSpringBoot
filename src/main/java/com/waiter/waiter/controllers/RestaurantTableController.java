package com.waiter.waiter.controllers;

import com.waiter.waiter.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Table;
import javax.validation.Valid;

@Controller
@RequestMapping("/tables")
public class RestaurantTableController {
    @Autowired
    RestaurantTableService restaurantTableService;
    @GetMapping
    private String getTables(Model model){
        //model.addAttribute("dishes", dishService.getAllDishes());
        //model.addAttribute("drinks", drinkService.getAllDrinks());
        return "/orders/restaurant-tables";
    }

    @GetMapping("/create")
    private String createDish(){
        restaurantTableService.createNewTable();
        return "/orders/restaurant-tables";
    }
}
