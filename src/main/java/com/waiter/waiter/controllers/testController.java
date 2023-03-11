package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import com.waiter.waiter.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class testController<DefaultOidcUser> {
    @Autowired
    RestaurantTablesRepository restaurantTablesRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @GetMapping("/test")
    public String getAllActors() {
        return "aa";
    }

    // @RequestMapping("/resource")
    //public Map<String, Object> home(Authentication authentication) {
        /*Map<String , Object> userDetails = ((DefaultOidcUser)authentication.getPrincipal()).getAttributes();
        System.out.println(userDetails.get("name"));
        System.out.println(userDetails.get("email"));
        System.out.println(userDetails.get("given_name"));*/
    @GetMapping("/hello")
    public String hello(@CurrentSecurityContext(expression="authentication?.name")
                        String username) {
        return "Hello, " + username + "!";
    }
    @GetMapping("/tablesTest")
    public String showTables(Model model, @RequestParam(required = false, defaultValue = "all") String filter) {
        Iterable<RestaurantTable> tables = new ArrayList<>();
        if (filter.equals("all")) {
            tables = restaurantTablesRepository.findAll();
        } else if (filter.equals("your")) {
            tables = orderRepository.getWaiterTables(userDetailsService.getLoggedUser().getId());
        }
        model.addAttribute("restaurantTables", tables);
        model.addAttribute("filter", filter);
        return "aa";
    }

}
