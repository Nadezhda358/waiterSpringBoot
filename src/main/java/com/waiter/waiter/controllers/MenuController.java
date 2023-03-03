package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.enums.DishType;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.services.DishService;
import com.waiter.waiter.services.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

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
