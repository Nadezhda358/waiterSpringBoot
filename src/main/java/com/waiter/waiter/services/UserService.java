package com.waiter.waiter.services;

import com.waiter.waiter.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public void saveUser(User user);
    public List<Object> isUserPresent(User user);
}
