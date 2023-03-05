package com.waiter.waiter.services;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class RestaurantTableService {
    @Autowired
    RestaurantTablesRepository restaurantTablesRepository;
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
    public RestaurantTable getTableById(Integer tId){
        Optional<RestaurantTable> restaurantTable=restaurantTablesRepository.findById(tId);
        if(restaurantTable.isPresent()) {
            return restaurantTable.get();
        } else {
            return new RestaurantTable();
        }
    }
}
