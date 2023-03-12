package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.User;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.repositories.UserRepository;
import com.waiter.waiter.services.DishService;
import com.waiter.waiter.services.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    DishService dishService;
    @Autowired
    DrinkService drinkService;
    @Autowired
    UserRepository userRepository;
    @GetMapping
    private String getMenu(Model model) {
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("drinks", drinkService.getAllDrinks());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user=userRepository.getUserByUsername(username);
        model.addAttribute("loggedUser", user);

        return "/menu/menu";
    }
}
