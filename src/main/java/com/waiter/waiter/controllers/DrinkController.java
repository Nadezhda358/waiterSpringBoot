package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Drink;
import com.waiter.waiter.services.DrinkService;
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
        return drinkService.saveDrink(drink, bindingResult, "/menu/add-drink");
    }
    @GetMapping("/menu")
    private String getMenu(){
        return "/menu/menu";
    }




    /*@PostMapping("/more-info/{drinkId}")
    private String moreInfo(@PathVariable(name="drinkId") Integer drinkId, Model model) {
        model.addAttribute("drink", drinkService.getDrinkById(drinkId));
        return "/menu/drink-info";
    }*/
    @GetMapping("/more-info/{drinkId}")
    private String moreInfo(@PathVariable(name="drinkId") Integer drinkId, Model model) {
        model.addAttribute("drink", drinkService.getDrinkById(drinkId));
        return "/menu/drink-info";
    }
    @GetMapping("/edit/{drinkId}")
    public String editDrink(@PathVariable(name="drinkId") Integer drinkId, Model model) {
        model.addAttribute("drink", drinkService.getDrinkById(drinkId));
        return "/menu/edit-drink";
    }
    @PostMapping("/update")
    private String updateDrink(@Valid Drink drink, BindingResult bindingResult) {
        return drinkService.saveDrink(drink, bindingResult, "/menu/edit-drink");
    }
    @PostMapping("/delete/{drinkId}")
    private String deleteDrink(@PathVariable(name="drinkId") Integer drinkId) {
        drinkService.deleteDrinkById(drinkId);
        return "redirect:/menu";
    }
}
