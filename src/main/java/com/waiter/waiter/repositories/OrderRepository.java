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

}
