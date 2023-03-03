package com.waiter.waiter.controllers;

import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.services.DishService;
import com.waiter.waiter.services.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    DishRepository dishRepository;
    @Autowired
    DishService dishService;
    @Autowired
    DrinkService drinkService;
    @GetMapping
    private String getMenu(Model model){
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("drinks", drinkService.getAllDrinks());
        return "/menu/menu";
    }
}
