package com.waiter.waiter.services;

import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
