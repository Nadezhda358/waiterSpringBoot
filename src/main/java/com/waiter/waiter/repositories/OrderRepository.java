package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.RestaurantTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    @Query("SELECT o FROM Order o where o.table=:t AND o.orderStatus <> 'PAID'")
    Order getOrderByTableId(@Param("t") Optional<RestaurantTable> t);
    @Query("SELECT o.table FROM Order o WHERE o.waiter.id=:waiterId AND o.orderStatus <> 'PAID'")
    Iterable<RestaurantTable> getWaiterTables(@Param("waiterId") Integer id);
    @Query("SELECT o FROM Order o WHERE o.orderStatus <> 'PAID'")
    Iterable<Order> getAllActiveOrdersForWaiter();
    @Query("SELECT o FROM Order o WHERE o.orderStatus <> 'PAID' AND o.waiter.id=:waiterId")
    Iterable<Order> getActiveOrdersForCertainWaiter(@Param("waiterId") Integer id);
    @Query("SELECT o FROM Order o WHERE o.orderStatus = 'TAKEN' OR o.orderStatus = 'COOKING'") //OR o.cook is null")
    Iterable<Order> getAllActiveOrdersForCook();
    @Query("SELECT o FROM Order o WHERE (o.orderStatus = 'TAKEN' OR o.orderStatus = 'COOKING') AND o.cook.id=:cookId") //OR o.cook is null")
    Iterable<Order> getActiveOrdersForCertainCook(@Param("cookId") Integer id);
    @Query("SELECT o FROM Order o WHERE o.orderStatus = 'TAKEN' AND o.cook.id is null") //OR o.cook is null")
    Iterable<Order> getActiveOrderWithoutCook();
}
