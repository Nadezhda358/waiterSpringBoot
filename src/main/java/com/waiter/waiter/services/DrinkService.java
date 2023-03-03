package com.waiter.waiter.services;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Drink;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.repositories.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class DrinkService {
    @Autowired
    DrinkRepository drinkRepository;

    public String saveDrink(@Valid Drink drink, BindingResult bindingResult, String redirectIfErrors){
        if (bindingResult.hasErrors()){
            return redirectIfErrors;
        }
        drinkRepository.save(drink);
        return "redirect:/menu";
    }
    public Iterable<Drink> getAllDrinks() {
        Iterable<Drink> drinks = drinkRepository.findAll();
        return drinks;
    }
    public Drink getDrinkById(Integer drinkId){
        Optional<Drink> oe = drinkRepository.findById(drinkId);
        if(oe.isPresent()) {
            return oe.get();
        } else {
            return new Drink();
        }
    }
    public void deleteDrinkById(Integer drinkId){
        drinkRepository.deleteById(drinkId);
    }
}
