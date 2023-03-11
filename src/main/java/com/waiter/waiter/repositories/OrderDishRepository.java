package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import com.waiter.waiter.entities.RestaurantTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderDishRepository extends CrudRepository<OrderDish, Integer> {
    @Query("SELECT o FROM OrderDish o where o.order=:order")
    List<OrderDish> getOrderInfo(@Param("order") Order order);

    @Query("SELECT d FROM Dish d WHERE d.id NOT IN (SELECT od.dish.id FROM OrderDish od WHERE od.order = :order)")
    public List<Dish> getAllNotAddedDishesToOrder(@Param("order") Order order);

    @Query("SELECT sum(od.currentPrice) FROM OrderDish od WHERE od.order = :order")
    public double getTotalCost(@Param("order") Order order);
}
