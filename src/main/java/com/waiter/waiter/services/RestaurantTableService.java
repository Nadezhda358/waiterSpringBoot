package com.waiter.waiter.services;

import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class RestaurantTableService {
    @Autowired
    RestaurantTablesRepository restaurantTablesRepository;
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
        Iterable<RestaurantTable> tables = switch (filter) {
            case "all" -> restaurantTablesRepository.findAll();
            case "your" -> orderRepository.getWaiterTables(userDetailsService.getLoggedUser().getId());
            case "notTaken" -> restaurantTablesRepository.getFreeTables();
            default -> new ArrayList<>();
        };
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
