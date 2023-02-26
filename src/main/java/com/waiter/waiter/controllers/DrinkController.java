package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Drink;
import com.waiter.waiter.services.DishService;
import com.waiter.waiter.services.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/drinks")
public class DrinkController {
    @Autowired
    DrinkService drinkService;

    @GetMapping("/create")
    private String createDrink(Model model){
        Drink drink = new Drink();
        model.addAttribute("drink", drink);
        return "/menu/add-drink";
    }
    @PostMapping("/submit")
    private String saveDrink(@Valid Drink drink, BindingResult bindingResult){
        return drinkService.saveDrink(drink, bindingResult);
    }
}
