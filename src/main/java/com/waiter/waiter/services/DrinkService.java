package com.waiter.waiter.services;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Drink;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.repositories.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

@Service
public class DrinkService {
    @Autowired
    DrinkRepository drinkRepository;

    public String saveDrink(@Valid Drink drink, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "/menu/add-drink";
        }
        drinkRepository.save(drink);
        return "index";
    }
}
