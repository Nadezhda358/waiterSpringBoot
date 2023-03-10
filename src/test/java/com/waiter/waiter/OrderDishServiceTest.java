package com.waiter.waiter;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.repositories.OrderDishRepository;
import com.waiter.waiter.services.OrderDishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OrderDishServiceTest {
    @Mock
    OrderDishRepository orderDishRepositoryMock;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    //@Test
    //void testSaveDishesToOrder() {
    //    OrderDishService orderDishService = new OrderDishService();
    //    Order order = new Order();
//
    //    Dish dish1 = new Dish();
    //    dish1.setId(1);
    //    dish1.setName("dish1");
//
    //    Dish dish2 = new Dish();
    //    dish1.setId(2);
    //    dish1.setName("dish2");
//
    //    List<Dish> dishes = new ArrayList<>();
    //    dishes.add(dish1);
    //    dishes.add(dish2);
//
    //    OrderDishHelp orderDishHelp = new OrderDishHelp();
    //    orderDishHelp.setOrder(order);
    //    orderDishHelp.setDishes(dishes);
//
    //    orderDishService.saveDishesToOrder(orderDishHelp);
//
    //    verify(orderDishRepositoryMock, times(2)).save(any(OrderDish.class));
    //}

}
