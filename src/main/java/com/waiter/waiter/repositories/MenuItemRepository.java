package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.MenuItem;
import org.springframework.data.repository.CrudRepository;


public interface MenuItemRepository extends CrudRepository<MenuItem, Integer> {
}
