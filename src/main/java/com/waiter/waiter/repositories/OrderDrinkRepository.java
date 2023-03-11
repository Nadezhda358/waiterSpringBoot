package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDrinkRepository extends CrudRepository<OrderDrink, Integer> {
    @Query("SELECT o FROM OrderDrink o where o.order=:order")
    List<OrderDrink> getOrderInfo(@Param("order") Order order);

    @Query("SELECT d FROM Drink d WHERE d.id NOT IN (SELECT od.drink.id FROM OrderDrink od WHERE od.order = :order)")
    public List<Drink> getAllNotAddedDrinksToOrder(@Param("order") Order order);

    @Query("SELECT sum(od.currentPrice) FROM OrderDrink od WHERE od.order = :order")
    public double getTotalCost(@Param("order") Order order);
}
