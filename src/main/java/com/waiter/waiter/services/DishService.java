package com.waiter.waiter.services;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class DishService {
    @Autowired
    DishRepository dishRepository;

    public String saveDish(@Valid Dish dish, BindingResult bindingResult, String redirectIfErrors){
        if (bindingResult.hasErrors()){
            return redirectIfErrors;
        }
        dishRepository.save(dish);
        return "redirect:/menu";
    }

    public Iterable<Dish> getAllDishes() {
        Iterable<Dish> dishes = dishRepository.findAll();
        return dishes;
    }
    public Dish getDishById(Integer dishId){
        Optional<Dish> oe = dishRepository.findById(dishId);
        if(oe.isPresent()) {
            return oe.get();
        } else {
            return new Dish();
        }
    }
    public void deleteDishById(Integer dishId){
        dishRepository.deleteById(dishId);
    }
}
