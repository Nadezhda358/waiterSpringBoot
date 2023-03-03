package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return dishService.saveDish(dish, bindingResult, "/menu/add-dish");
    }
    @PostMapping("/more-info/{dishId}")
    private String moreInfo(@PathVariable(name="dishId") Integer dishId, Model model) {
        model.addAttribute("dish", dishService.getDishById(dishId));
        return "/menu/dish-info";
    }
    @GetMapping("/edit/{dishId}")
    public String editDish(@PathVariable(name="dishId") Integer dishId, Model model) {
        model.addAttribute("dish", dishService.getDishById(dishId));
        return "/menu/edit-dish";
    }
    @PostMapping("/update")
    private String updateDish(@Valid Dish dish, BindingResult bindingResult) {
        return dishService.saveDish(dish, bindingResult, "/menu/edit-dish");
    }
    @PostMapping("/delete/{dishId}")
    private String deleteDish(@PathVariable(name="dishId") Integer dishId) {
        dishService.deleteDishById(dishId);
        return "redirect:/menu";
    }
}
