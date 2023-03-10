package com.waiter.waiter.services;

import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilterService {
    private String filter;
    @Autowired
    RestaurantTablesRepository restaurantTablesRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    OrderRepository orderRepository;
    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Iterable<RestaurantTable> filter(){
        if (filter.equalsIgnoreCase("all")){
            return restaurantTablesRepository.findAll();
        }else if (filter.equalsIgnoreCase("yourTables")){
            return orderRepository.getWaiterTables(userDetailsService.getLoggedUser().getId());
         }
        return restaurantTablesRepository.findAll();
    }
}
