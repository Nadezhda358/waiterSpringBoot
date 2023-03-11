package com.waiter.waiter.services;

import com.waiter.waiter.entities.*;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.helpingClasses.OrderDrinkHelp;
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
public class OrderDrinkService {
    @Autowired
    OrderDrinkRepository orderDrinkRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;

    public void saveDrinksToOrder(OrderDrinkHelp orderDrinkHelp) {
        Order order=orderDrinkHelp.getOrder();
        List<Drink> drinks=orderDrinkHelp.getDrinks();
        for (Drink d:drinks) {
            OrderDrink orderDrink=new OrderDrink();
            orderDrink.setOrder(order);
            orderDrink.setQuantity(1);
            orderDrink.setCurrentPrice(d.getPrice());
            orderDrink.setPricePerItem(d.getPrice());
            orderDrink.setDrink(d);
            orderDrinkRepository.save(orderDrink);
        }
    }
    public List<OrderDrink> getOrderInfo(Order order){
        List<OrderDrink> orderDrinks1=orderDrinkRepository.getOrderInfo(order);
        List<OrderDrink> orderInfo=new ArrayList<>();
        if(orderDrinks1!=null){
            orderInfo.addAll(orderDrinks1);
        }
        return  orderInfo;
    }
    public OrderDrink getOrderDrinkById(Integer orderDrinkId){
        Optional<OrderDrink> oe = orderDrinkRepository.findById(orderDrinkId);
        if(oe.isPresent()) {
            return oe.get();
        } else {
            return new OrderDrink();
        }
    }

    public Iterable<Drink> findAllNotAddedDrinksToOrder(Order order) {
        Iterable<Drink> drinks=orderDrinkRepository.getAllNotAddedDrinksToOrder(order);
        for (Drink d: drinks) {
            d.toString();
        }
        return drinks;
    }
    public void deleteOrderDrinkById(Integer orderDrinkId,int orderId){
        OrderDrink orderDrink = getOrderDrinkById(orderDrinkId);
        if (orderDrink.getOrder().getOrderStatus() == OrderStatus.TAKING) {
            orderDrinkRepository.deleteById(orderDrinkId);
        }
    }

    public void prepareToAddDrinks(Integer orderId, Model model) {

        Order order=orderService.getOrderById(orderId);//todo prepareToAddDrinks see if this is fine

        Iterable<Drink> selectableDrinks = findAllNotAddedDrinksToOrder(order);

        OrderDrinkHelp orderDrinkHelp = new OrderDrinkHelp(order);
        model.addAttribute("orderdrink", orderDrinkHelp);
        model.addAttribute("selectableDrinks", selectableDrinks);
        model.addAttribute("order", order);
    }

    public void saveAddedDrinks(Integer orderId, OrderDrinkHelp orderDrinkHelp, Model model) {
        Order order=orderService.getOrderById(orderId);
        orderDrinkHelp.setOrder(order);
        saveDrinksToOrder(orderDrinkHelp);
        order.setTotalCost(orderDrinkRepository.getTotalCost(order));
        orderRepository.save(order);

        model.addAttribute("order", order);
        model.addAttribute("orderDrink", getOrderInfo(order));
        boolean orderDrinkNull = false;
        if (getOrderInfo(order) == null) {
            orderDrinkNull = true;
        }
        model.addAttribute("orderDrinkNull", orderDrinkNull);
    }

    public int findOrderIdByOrderDrinkId(Integer orderDrinkId) {
        Optional<OrderDrink> orderDrink = orderDrinkRepository.findById(orderDrinkId);
        if(orderDrink.isPresent()) {
            return orderDrink.get().getOrder().getId();
        } else {
            return 0;
        }
    }

    public void addOneToQuantity(Integer orderDrinkId) {
        Optional<OrderDrink> orderDrinks = orderDrinkRepository.findById(orderDrinkId);
        OrderDrink orderDrink;
        if (orderDrinks.isPresent()) {
            orderDrink = orderDrinks.get();
        } else {
            orderDrink = new OrderDrink();
        }
        orderDrink.setQuantity(orderDrink.getQuantity() + 1);
        orderDrink.setCurrentPrice(orderDrink.getQuantity() * orderDrink.getPricePerItem());
        orderDrinkRepository.save(orderDrink);

        Order order=orderDrink.getOrder();
        order.setTotalCost(orderDrinkRepository.getTotalCost(order));
        orderRepository.save(order);
    }

    public void removeOneFromQuantity(Integer orderDrinkId) {
        OrderDrink orderDrink=getOrderDrinkById(orderDrinkId);
        if (orderDrink.getQuantity() > 1) {
            orderDrink.setQuantity(orderDrink.getQuantity() - 1);
            orderDrink.setCurrentPrice(orderDrink.getQuantity() * orderDrink.getPricePerItem());
            orderDrinkRepository.save(orderDrink);

            Order order=orderDrink.getOrder();
            order.setTotalCost(orderDrinkRepository.getTotalCost(order));
            orderRepository.save(order);
        }
    }
}
