package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {
    User getUserByUsername(@Param("username") String username);

}
