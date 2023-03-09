package com.waiter.waiter.services;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.enums.Role;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import com.waiter.waiter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RestaurantTablesRepository restaurantTablesRepository;

    public Order getOrderByTableId(Optional<RestaurantTable> tId){
        Order order = orderRepository.getOrderByTableId(tId);
            return order;
    }
    public boolean isAbleToChangeStatus(OrderStatus status, boolean isWaiter){
        if (isWaiter && (status==OrderStatus.TAKING || status==OrderStatus.COOKED || status==OrderStatus.SERVED)){
            return true;
        }else if(!isWaiter && (status==OrderStatus.TAKEN || status==OrderStatus.COOKING)) {
            return true;
        }
        return false;
    }
    public void ChangeStatus(Order order){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        com.waiter.waiter.entities.User user = userRepository.getUserByUsername(username);

        if (isAbleToChangeStatus(order.getOrderStatus(), (user.getRole() == Role.WAITER))){
            switch (order.getOrderStatus()){
                case TAKING -> order.setOrderStatus(OrderStatus.TAKEN);
                case TAKEN -> {
                    order.setOrderStatus(OrderStatus.COOKING);
                    order.setCook(user);
                }
                case COOKING -> order.setOrderStatus(OrderStatus.COOKED);
                case COOKED -> order.setOrderStatus(OrderStatus.SERVED);
                case SERVED -> {
                    order.setOrderStatus(OrderStatus.PAID);
                    order.getTable().setHasOrder(false);
                    restaurantTablesRepository.save(order.getTable());
                    order.setFinishDate(LocalDateTime.now());
                }
            }
        }
        orderRepository.save(order);
    }
}
