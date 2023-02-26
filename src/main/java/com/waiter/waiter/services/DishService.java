package com.waiter.waiter.services;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

@Service
public class DishService {
    @Autowired
    DishRepository dishRepository;

    public String saveDish(@Valid Dish dish, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "/menu/add-dish";
        }
        dishRepository.save(dish);
        return "index";
    }
}
