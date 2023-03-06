package com.waiter.waiter.services;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.repositories.OrderDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDishService {
    @Autowired
    OrderDishRepository orderDishRepository;
@Autowired
    DishRepository dishRepository;

    public void saveDishesToOrder(OrderDishHelp orderDishHelp) {
        Order order=orderDishHelp.getOrder();
        List<Dish> dishes=orderDishHelp.getDishes();
        for (Dish d:dishes) {
            OrderDish orderDish=new OrderDish();
            orderDish.setOrder(order);
            orderDish.setQuantity(1);
            orderDish.setCurrentPrice(d.getPrice());
            orderDish.setDish(d);
            orderDishRepository.save(orderDish);
        }
    }
    public List<OrderDish> getOrderInfo(Order order){
        List<OrderDish> orderDishes1=orderDishRepository.getOrderInfo(order);
        List<OrderDish> orderInfo=new ArrayList<>();
        if(orderDishes1!=null){
            orderInfo.addAll(orderDishes1);
        }
        return  orderInfo;
    }

    public Iterable<Dish> findAllNotAddedDishesToOrder(Order order) {
        Iterable<Dish> dishes=orderDishRepository.getAllNotAddedDishesToOrder(order);
        for (Dish d: dishes) {
            d.toString();
        }
        return dishes;
    }
}
