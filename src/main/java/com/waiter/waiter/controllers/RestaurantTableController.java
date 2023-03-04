package com.waiter.waiter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tables")
public class RestaurantTableController {
    @GetMapping
    private String getTables(Model model){
        //model.addAttribute("dishes", dishService.getAllDishes());
        //model.addAttribute("drinks", drinkService.getAllDrinks());
        return "/orders/restaurant-tables";
    }
}
