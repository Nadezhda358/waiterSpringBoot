package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.repositories.MenuItemRepository;
import com.waiter.waiter.services.DishService;
import com.waiter.waiter.services.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/dishes")
public class DishController {
    @Autowired
    DishService dishService;

    @GetMapping("/create")
    private String createDish(Model model){
        Dish dish = new Dish();
        model.addAttribute("dish", dish);
        return "/menu/add-dish";
    }
    @PostMapping("/submit")
    private String saveDish(@Valid Dish dish, BindingResult bindingResult){
        return dishService.saveDish(dish, bindingResult);
    }

}
