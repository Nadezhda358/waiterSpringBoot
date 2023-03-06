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

    //@Query("Select od.Dish from OrderDish od join Dish d on od.getDish.getId=d.getId where od.getOrder!=:order")
    @Query("Select OrderDish.Dish from OrderDish od where od.getOrder=:order")
    List<Dish> getAllNotAddedDishesToOrder(Order order);
}
