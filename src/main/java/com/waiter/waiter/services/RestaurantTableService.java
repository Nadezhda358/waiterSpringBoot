package com.waiter.waiter.services;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class RestaurantTableService {
    @Autowired
    RestaurantTablesRepository restaurantTablesRepository;
    @Autowired
    FilterService filterService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    public void createNewTable(){
        int newTableNumber = restaurantTablesRepository.count() > 0 ? restaurantTablesRepository.getLastTableNumber() + 1 : 1;
        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setNumber(newTableNumber);
        restaurantTablesRepository.save(restaurantTable);
    }

    public Iterable<RestaurantTable> getAllTables(){
        Iterable<RestaurantTable> restaurantTables=restaurantTablesRepository.findAll();
        return  restaurantTables;
    }
    public Iterable<RestaurantTable> getTables(String filter){
        Iterable<RestaurantTable> tables = new ArrayList<>();
        if (filter.equals("all")) {
            tables = restaurantTablesRepository.findAll();
        } else if (filter.equals("your")) {
            tables = orderRepository.getWaiterTables(userDetailsService.getLoggedUser().getId());
        }
        return tables;
    }
    public RestaurantTable getTableById(Integer tId){
        Optional<RestaurantTable> restaurantTable=restaurantTablesRepository.findById(tId);
        if(restaurantTable.isPresent()) {
            return restaurantTable.get();
        } else {
            return new RestaurantTable();
        }
    }
}
