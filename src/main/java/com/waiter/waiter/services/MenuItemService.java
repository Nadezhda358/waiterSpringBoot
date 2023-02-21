package com.waiter.waiter.services;

import com.waiter.waiter.entities.MenuItem;
import com.waiter.waiter.repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

@Service
public class MenuItemService {
    @Autowired
    MenuItemRepository menuItemRepository;

    public String saveMenuItem(@Valid MenuItem menuItem, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "add-menu-item";
        }
        menuItemRepository.save(menuItem);
        return "index";
    }
}
