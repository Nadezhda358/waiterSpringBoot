package com.waiter.waiter.services;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.repositories.OrderDishRepository;
import com.waiter.waiter.repositories.OrderDrinkRepository;
import com.waiter.waiter.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDishService {
    @Autowired
    OrderDishRepository orderDishRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDrinkRepository orderDrinkRepository;

    public void saveDishesToOrder(OrderDishHelp orderDishHelp) {
        Order order = orderDishHelp.getOrder();
        List<Dish> dishes = orderDishHelp.getDishes();
        for (Dish d : dishes) {
            OrderDish orderDish = new OrderDish();
            orderDish.setOrder(order);
            orderDish.setQuantity(1);
            orderDish.setCurrentPrice(d.getPrice());
            orderDish.setPricePerItem(d.getPrice());
            orderDish.setDish(d);
            orderDishRepository.save(orderDish);
        }
    }

    public List<OrderDish> getOrderInfo(Order order) {
        List<OrderDish> orderDishes1 = orderDishRepository.getOrderInfo(order);
        List<OrderDish> orderInfo = new ArrayList<>();
        if (orderDishes1 != null) {
            orderInfo.addAll(orderDishes1);
        }
        return orderInfo;
    }

    public OrderDish getOrderDishById(Integer orderDishId) {
        Optional<OrderDish> oe = orderDishRepository.findById(orderDishId);
        if (oe.isPresent()) {
            return oe.get();
        } else {
            return new OrderDish();
        }
    }

    public Iterable<Dish> findAllNotAddedDishesToOrder(Order order) {
        Iterable<Dish> dishes = orderDishRepository.getAllNotAddedDishesToOrder(order);
        for (Dish d : dishes) {
            d.toString();
        }
        return dishes;
    }

    public void deleteOrderDishById(Integer orderDishId, int orderId) {
        OrderDish orderDish = getOrderDishById(orderDishId);
        if (orderDish.getOrder().getOrderStatus() == OrderStatus.TAKING) {
            orderDishRepository.deleteById(orderDishId);
        }
        Optional<Order> orders = orderRepository.findById(orderId);
        Order order = orders.get();
        updateTotalCostOrder(order);
    }

    public void prepareToAddDishes(Integer orderId, Model model) {

        Optional<Order> orders = orderRepository.findById(orderId);
        Order order = orders.get();

        Iterable<Dish> selectableDishes = findAllNotAddedDishesToOrder(order);

        OrderDishHelp orderDishHelp = new OrderDishHelp(order);
        model.addAttribute("orderdish", orderDishHelp);
        model.addAttribute("selectableDishes", selectableDishes);
        model.addAttribute("order", order);
    }

    public void saveAddedDishes(Integer orderId, OrderDishHelp orderDishHelp, Model model) {
        Optional<Order> orders = orderRepository.findById(orderId);
        Order order = orders.get();
        orderDishHelp.setOrder(order);
        saveDishesToOrder(orderDishHelp);
        updateTotalCostOrder(order);
    }

    public int findOrderIdByOrderDishId(Integer orderDishId) {
        Optional<OrderDish> orderDish = orderDishRepository.findById(orderDishId);
        if (orderDish.isPresent()) {
            return orderDish.get().getOrder().getId();
        } else {
            return 0;
        }
    }

    public void addOneToQuantity(Integer orderDishId) {
        Optional<OrderDish> orderDishes = orderDishRepository.findById(orderDishId);
        OrderDish orderDish;
        if (orderDishes.isPresent()) {
            orderDish = orderDishes.get();
        } else {
            orderDish = new OrderDish();
        }
        orderDish.setQuantity(orderDish.getQuantity() + 1);
        orderDish.setCurrentPrice(orderDish.getQuantity() * orderDish.getPricePerItem());
        orderDishRepository.save(orderDish);

        Order order = orderDish.getOrder();
        updateTotalCostOrder(order);
    }

    public void removeOneFromQuantity(Integer orderDishId) {
        Optional<OrderDish> orderDishes = orderDishRepository.findById(orderDishId);
        OrderDish orderDish;
        if (orderDishes.isPresent()) {
            orderDish = orderDishes.get();
        } else {
            orderDish = new OrderDish();
        }
        if (orderDish.getQuantity() > 1) {
            orderDish.setQuantity(orderDish.getQuantity() - 1);
            orderDish.setCurrentPrice(orderDish.getQuantity() * orderDish.getPricePerItem());
            orderDishRepository.save(orderDish);

            Order order = orderDish.getOrder();
            updateTotalCostOrder(order);
        }
    }

    public void updateTotalCostOrder(Order order) {
        double totalCost = 0;
        if (orderDishRepository.getTotalCost(order).isPresent()) {
            totalCost += orderDishRepository.getTotalCost(order).get();
        }
        if (orderDrinkRepository.getTotalCost(order).isPresent()) {
            totalCost += orderDrinkRepository.getTotalCost(order).get();
        }
        order.setTotalCost(totalCost);
        orderRepository.save(order);
    }
}
