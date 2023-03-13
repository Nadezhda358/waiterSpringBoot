package com.waiter.waiter.services;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    //Не се ползва
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
    //public int getTableIdByOrderId(Integer orderId) {
    //    Optional<Order> order1=orderRepository.findById(orderId);
    //    Order order;
    //    if(order1.isPresent()) {
    //        order=order1.get();
    //    } else {
    //        order=new Order();
    //    }
    //    return order.getTable().getId();
    //}
    public int getTableIdByOrderId(int orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getTable() != null) {
                return order.getTable().getId();
            }
        }
        return -1;
    }
    public void setCardColorsForTables(){
        Iterable<RestaurantTable> tables = restaurantTablesRepository.findAll();
        for (RestaurantTable table: tables) {
            if (table.isHasOrder()){
                LocalDateTime updatedOn = orderRepository.getUpdatedOnByTable(Optional.of(table));
                Duration timeDiff = Duration.between(updatedOn, LocalDateTime.now());
                boolean isTimeDiffGreaterThan30Minutes = timeDiff.compareTo(Duration.ofMinutes(30)) >= 0;
                table.setTableCardBackground(isTimeDiffGreaterThan30Minutes ? "card-red" : "card-green");
            }else{
                table.setTableCardBackground("card-white");
            }
        }
        restaurantTablesRepository.saveAll(tables);
    }
}
