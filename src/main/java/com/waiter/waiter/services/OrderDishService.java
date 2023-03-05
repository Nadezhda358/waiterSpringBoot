package com.waiter.waiter.services;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.repositories.OrderDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class OrderDishService {
    @Autowired
    OrderDishRepository orderDishRepository;


    public String saveDishesToOrder(OrderDishHelp orderDishHelp, String s) {
        Order order=orderDishHelp.getOrder();
        //System.out.println("Order id = "+orderDishHelp.getOrder().getId());
        for (Dish d:orderDishHelp.getDishes()) {
            System.out.println("Dish id = "+d.getId()+" and name = "+d.getName());
        }
        System.out.println();
        List<Dish> dishes=orderDishHelp.getDishes();
        for (Dish d:dishes) {
            OrderDish orderDish=new OrderDish();
            orderDish.setOrder(order);
            orderDish.setDish(d);
            orderDishRepository.save(orderDish);
        }
        return s;
    }
}
