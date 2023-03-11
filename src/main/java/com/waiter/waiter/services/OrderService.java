package com.waiter.waiter.services;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.entities.User;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.enums.Role;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import com.waiter.waiter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    RestaurantTableService restaurantTableService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    RestaurantTablesRepository restaurantTablesRepository;
    @Autowired
    OrderDishService orderDishService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    public Order getOrderByTableId(Optional<RestaurantTable> tId){
        Order order = orderRepository.getOrderByTableId(tId);
            return order;
    }
    public boolean isAbleToChangeStatus(OrderStatus status,  User orderWaiter,User orderCook,User loggedUser){
        if (loggedUser.getRole()==Role.WAITER && loggedUser==orderWaiter &&(status==OrderStatus.TAKING || status==OrderStatus.COOKED || status==OrderStatus.SERVED)){
            return true;
        }else if(loggedUser.getRole()==Role.COOK && (status==OrderStatus.TAKEN || status==OrderStatus.COOKING)) {
            if(status==OrderStatus.COOKING && loggedUser!=orderCook) {
                return false;
            }
            return true;
        }
        return false;
    }
    public void ChangeStatus(Order order){
       User loggedUser=userDetailsService.getLoggedUser();
       if (isAbleToChangeStatus(order.getOrderStatus(), order.getWaiter(),order.getCook(),loggedUser)){
            switch (order.getOrderStatus()){
                case TAKING -> order.setOrderStatus(OrderStatus.TAKEN);
                case TAKEN -> {
                    order.setOrderStatus(OrderStatus.COOKING);
                    order.setCook(loggedUser);
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

    public void viewOrderByTableId(Integer tId, Model model) {
        Optional<RestaurantTable> t = restaurantTablesRepository.findById(tId);
        Order order = getOrderByTableId(t);
        model.addAttribute("order", order);

        List<OrderDish> orderDish= orderDishService.getOrderInfo(order);
        model.addAttribute("orderDish", orderDish);

        boolean orderDishNull = isOrderDishNull(orderDish);
        model.addAttribute("isOrderDishNull", orderDishNull);

        User loggedUser = userDetailsService.getLoggedUser();
        model.addAttribute("loggedUser", loggedUser);

        boolean isAbleToChangeStatus = isAbleToChangeStatus(order.getOrderStatus(), order.getWaiter(),order.getCook(),loggedUser);
        model.addAttribute("isAbleToChangeStatus", isAbleToChangeStatus);
    }
    public boolean isOrderDishNull(List<OrderDish> orderDish){
        if (orderDish.isEmpty()) {
            return true;
        }
        return  false;
    }

    public void createOrder(Integer tId) {
        RestaurantTable table = restaurantTableService.getTableById(tId);
        User user=userDetailsService.getLoggedUser();
        Order order = new Order(table,user);
        table.setHasOrder(true);
        restaurantTablesRepository.save(table);
        orderRepository.save(order);
    }

    public Order getOrderById(int orderId) {
        Optional<Order> order1 = orderRepository.findById(orderId);
        Order order;
        if(order1.isPresent()) {
            order=order1.get();
        } else {
            order=new Order();
        }
        return  order;
    }
}
