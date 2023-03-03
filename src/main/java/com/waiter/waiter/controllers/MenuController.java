package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.enums.DishType;
import com.waiter.waiter.repositories.DishRepository;
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
    @GetMapping
    private String getMenu(Model model){
        List<Dish> dishes = dishRepository.getDishes();
        model.addAttribute("dishes", dishes);

        //List<Dish> salads = dishRepository.getDishesByType("SALAD");
        //model.addAttribute("salads", salads);

        //ArrayList<ArrayList<Dish>> dishesByTypes = new ArrayList<>();
        //for (int i = 0; i < DishType.values().length; i++) {
        //    ArrayList<Dish> dishes = dishRepository.getDishesByType(DishType.)
        //    dishesByTypes.add()
        //}
        //EnumMap<DishType, ArrayList<Dish>> dishItems =  new EnumMap<>(DishType.class);
        //for (int i = 0; i < DishType.values().length; i++) {
        //    dishItems.computeIfAbsent(drinkToAdd.getDrinkType(), k -> new ArrayList<>()).add(drinkToAdd);
        //}
        return "/menu/menu";
    }
}
