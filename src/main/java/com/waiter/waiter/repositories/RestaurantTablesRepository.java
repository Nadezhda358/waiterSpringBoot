package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.MenuItem;
import com.waiter.waiter.entities.RestaurantTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantTablesRepository extends CrudRepository<RestaurantTable, Integer> {
    @Query("SELECT MAX(t.number) FROM RestaurantTable t")
    int getLastTableNumber();
}
