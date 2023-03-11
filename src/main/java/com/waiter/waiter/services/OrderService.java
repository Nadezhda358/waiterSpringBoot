package com.waiter.waiter.services;

import com.waiter.waiter.entities.*;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.enums.Role;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    OrderDrinkService orderDrinkService;
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

        List<OrderDrink> orderDrink= orderDrinkService.getOrderInfo(order);
        model.addAttribute("orderDrink", orderDrink);

        boolean orderDrinkNull = isOrderDrinkNull(orderDrink);
        model.addAttribute("isOrderDrinkNull", orderDrinkNull);

        User loggedUser = userDetailsService.getLoggedUser();
        model.addAttribute("loggedUser", loggedUser);

        boolean isAbleToChangeStatus = isAbleToChangeStatus(order.getOrderStatus(), order.getWaiter(),order.getCook(),loggedUser);
        model.addAttribute("isAbleToChangeStatus", isAbleToChangeStatus);
    }

    private boolean isOrderDrinkNull(List<OrderDrink> orderDrink) {
        if (orderDrink.isEmpty()) {
            return true;
        }
        return  false;
    }

    public boolean isOrderDishNull(List<OrderDish> orderDish){
        if (orderDish.isEmpty()) {
            return true;
        }
        return  false;
    }
    public Iterable<Order> getActiveOrders(String filter){
        Iterable<Order> orders = new ArrayList<>();
        if (userDetailsService.getLoggedUser().getRole() == Role.WAITER){
            if (filter.equalsIgnoreCase("all")){
                orders = orderRepository.getAllActiveOrdersForWaiter();
            } else if (filter.equalsIgnoreCase("your")) {
                orders = orderRepository.getActiveOrdersForCertainWaiter(userDetailsService.getLoggedUser().getId());
            }
        } else if (userDetailsService.getLoggedUser().getRole() == Role.COOK) {
            if (filter.equalsIgnoreCase("all")) {
                orders = orderRepository.getAllActiveOrdersForCook();
            }else if (filter.equalsIgnoreCase("your")){
                orders = orderRepository.getActiveOrdersForCertainCook(userDetailsService.getLoggedUser().getId());
            }else if (filter.equalsIgnoreCase("free")){
                orders = orderRepository.getActiveOrderWithoutCook();
            }
        }
        return orders;
    }
    public Iterable<Order> getOrdersForWaiterByDate(String filter1, String filter2){
        Iterable<Order> orders = new ArrayList<>();
        if (filter1.equalsIgnoreCase("all")){
            orders = orderRepository.findAll();
        } else if (filter1.equalsIgnoreCase("your")) {
            orders = orderRepository.getAllOrdersForCertainWaiter(userDetailsService.getLoggedUser().getId());
        }
        if (filter2.equalsIgnoreCase("newest")) {
            // sort the Iterable by createdOn in descending order
            orders = StreamSupport.stream(orders.spliterator(), false)
                    .sorted(Comparator.comparing(Order::getCreatedOn, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else if(filter2.equalsIgnoreCase("oldest")){
            // sort the Iterable by createdOn in ascending order
            orders = StreamSupport.stream(orders.spliterator(), false)
                    .sorted(Comparator.comparing(Order::getCreatedOn))
                    .collect(Collectors.toList());
        }
        return orders;
    }
    public Iterable<Object[]> getOrdersForCookByDate(String filter1){
        Iterable<Object[]> orders = new ArrayList<>();
        if (filter1.equalsIgnoreCase("all")){
            orders = orderRepository.getOrdersCountByDate();
        } else if (filter1.equalsIgnoreCase("your")) {
            orders = orderRepository.getOrdersCountByDateForCertainCook(userDetailsService.getLoggedUser().getId());
        }
        return orders;
    }

    public void createOrder(Integer tId) {
        RestaurantTable table = restaurantTableService.getTableById(tId);
        User user=userDetailsService.getLoggedUser();
        Order order = new Order(table,user);
        table.setHasOrder(true);
        restaurantTablesRepository.save(table);
        orderRepository.save(order);
    }
    public Iterable<Order> getOrdersForCertainDate(String date, String filter){
        Iterable<Order> orders = new ArrayList<>();
        if (filter.equalsIgnoreCase("all")){
            orders = orderRepository.getAllOrdersForCertainDate(date);
        } else if (filter.equalsIgnoreCase("your")) {
            orders = orderRepository.getCookOrdersForCertainDate(date, userDetailsService.getLoggedUser().getId());
        }
        return orders;
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
