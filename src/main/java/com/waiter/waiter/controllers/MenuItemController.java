package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.MenuItem;
import com.waiter.waiter.repositories.MenuItemRepository;
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
@RequestMapping("/menuItems")
public class MenuItemController {
    @Autowired
    MenuItemService menuItemService;
    @Autowired
    MenuItemRepository menuItemRepository;

    @GetMapping("/create")
    private String createMenuItem(Model model){
        MenuItem menuItem = new MenuItem();
        model.addAttribute("menuItem", menuItem);
        return "add-menu-item";
    }
    @PostMapping("/submit")
    private String saveMenuItem(@Valid MenuItem menuItem, BindingResult bindingResult){
        return menuItemService.saveMenuItem(menuItem, bindingResult);
    }
}
