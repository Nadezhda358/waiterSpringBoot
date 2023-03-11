package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.*;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.enums.Role;
import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.repositories.*;
import com.waiter.waiter.services.OrderDishService;
import com.waiter.waiter.services.OrderService;
import com.waiter.waiter.services.RestaurantTableService;
import com.waiter.waiter.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDishService orderDishService;
    @Autowired
    OrderDishRepository orderDishRepository;

    @PostMapping("/create/{tId}")
    private String createOrder(@PathVariable(name = "tId") Integer tId, Model model) {
       orderService.createOrder(tId);
        return "redirect:/orders/view/"+tId;
    }

    @PostMapping("/view/{tId}")
    private String viewOrderByTableId(@PathVariable(name = "tId") Integer tId, Model model) {
        orderService.viewOrderByTableId(tId, model);
        return "/orders/view-order";
    }
    @GetMapping("/view/{tId}")
    private String viewOrderByTableId1(@PathVariable(name = "tId") Integer tId, Model model) {
        orderService.viewOrderByTableId(tId, model);
        return "/orders/view-order";
    }

    @GetMapping
    public String getAllOrders(Model model) {
        Iterable<Order> orders = orderRepository.findAll();
    @PostMapping("/add-dishes/{orderId}")
    private String addDishesInOrder(@PathVariable(name = "orderId") Integer orderId, Model model) {
        Optional<Order> orders = orderRepository.findById(orderId);
        Order order = orders.get();
        //Iterable<Dish> dishes1=dishRepository.findAll();

        Iterable<Dish> dishes1 = orderDishService.findAllNotAddedDishesToOrder(order);
        /*if(dishes==null){
            return "";//ако няма ястия в менюто, да не се добавят ястия в поръчка
        }*/
        OrderDishHelp orderDishHelp = new OrderDishHelp();
        orderDishHelp.setOrder(order);
        model.addAttribute("orderdish", orderDishHelp);
        model.addAttribute("selectabledishes", dishes1);
        model.addAttribute("order", order);

        return "/orders/add-dish-to-order";
    }

    @PostMapping("/order-dish/add-to-order/submit/{orderId}")
    private String saveDishesToOrder(@PathVariable(name = "orderId") Integer orderId, OrderDishHelp orderDishHelp, Model model) {
        Optional<Order> orders = orderRepository.findById(orderId);
        Order order = orders.get();
        orderDishHelp.setOrder(order);
        orderDishService.saveDishesToOrder(orderDishHelp);
        order.setTotalCost(orderDishRepository.getTotalCost(order));
        orderRepository.save(order);


        model.addAttribute("order", order);
        model.addAttribute("orderDish", orderDishService.getOrderInfo(order));
        boolean orderDishNull = false;
        if (orderDishService.getOrderInfo(order) == null) {
            orderDishNull = true;
        }
        model.addAttribute("orderDishNull", orderDishNull);
        int tId=order.getTable().getId();
        return "redirect:/orders/view/"+tId;
    }

    //@GetMapping
    //public String getAllOrders(Model model) {  /reference-waiter
    //    Iterable<Order> orders = orderRepository.findAll();
    //    model.addAttribute("orders", orders);
    //    return "/orders/orders-list";
    //}
    @GetMapping("/active")
    public String getActiveOrders(@RequestParam(required = false, defaultValue = "your") String filter, Model model) {
        Iterable<Order> orders = orderService.getActiveOrders(filter);
        model.addAttribute("orders", orders);

        model.addAttribute("loggedUser", userDetailsService.getLoggedUser());
        model.addAttribute("filter", filter);

        return "/orders/orders-list";
    }
    @PostMapping("/delete-dishes/{orderDishId}")
    private String deleteDishesInOrder(@PathVariable(name = "orderDishId") Integer orderDishId,
                                       Model model) {
        Order order = orderDishRepository.findById(orderDishId).get().getOrder();

        orderDishService.deleteOrderDishById(orderDishId);

        order.setTotalCost(orderDishRepository.getTotalCost(order));
        orderRepository.save(order);

        model.addAttribute("order", order);
        model.addAttribute("orderDish", orderDishService.getOrderInfo(order));
        boolean orderDishNull = false;
        if (orderDishService.getOrderInfo(order) == null) {
            orderDishNull = true;
        }
        model.addAttribute("orderDishNull", orderDishNull);
        int tId=order.getTable().getId();
        return "redirect:/orders/view/"+tId;
    }

    @PostMapping("/add-one-to-quantity/{orderDishId}")
    private String addOneToQuantity(@PathVariable(name = "orderDishId") Integer orderDishId,
                                    Model model) {
        Order order = orderDishRepository.findById(orderDishId).get().getOrder();
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

        order.setTotalCost(orderDishRepository.getTotalCost(order));
        orderRepository.save(order);

        model.addAttribute("order", order);
        model.addAttribute("orderDish", orderDishService.getOrderInfo(order));
        boolean orderDishNull = false;
        if (orderDishService.getOrderInfo(order) == null) {
            orderDishNull = true;
        }
        model.addAttribute("orderDishNull", orderDishNull);
        int tId=order.getTable().getId();
        return "redirect:/orders/view/"+tId;
    }

    @PostMapping("/remove-one-from-quantity/{orderDishId}")
    private String removeOneFromQuantity(@PathVariable(name = "orderDishId") Integer orderDishId,
                                         Model model) {
        Order order = orderDishRepository.findById(orderDishId).get().getOrder();
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

            order.setTotalCost(orderDishRepository.getTotalCost(order));
            orderRepository.save(order);
        }

        model.addAttribute("order", order);
        model.addAttribute("orderDish", orderDishService.getOrderInfo(order));
        boolean orderDishNull = false;
        if (orderDishService.getOrderInfo(order) == null) {
            orderDishNull = true;
        }
        model.addAttribute("orderDishNull", orderDishNull);
        int tId=order.getTable().getId();
        return "redirect:/orders/view/"+tId;
    }

    @PostMapping("/change-status/{orderId}")
    public String changeStatus(@PathVariable(name = "orderId") Integer orderId, Model model) {
        Optional<Order> order = orderRepository.findById(orderId);
        Order order1;
        if (order.isPresent()) {
            order1 = order.get();
        } else {
            order1 = new Order();
        }

        orderService.ChangeStatus(order1);
        int tId=order1.getTable().getId();
        if(order1.getOrderStatus()==OrderStatus.PAID){
            return "redirect:/tables";
        }
        return "redirect:/orders/view/"+tId;
    }
    @GetMapping("/reference-waiter")
    public String getOrdersReferenceWaiter(Model model, @RequestParam(required = false, defaultValue = "your") String filter1, @RequestParam(required = false, defaultValue = "newest") String filter2) {
        Iterable<Order> orders = orderService.getOrdersForWaiterByDate(filter1, filter2);
        model.addAttribute("orders", orders);

        model.addAttribute("loggedUser", userDetailsService.getLoggedUser());
        model.addAttribute("filter1", filter1);
        return "/orders/orders-reference-waiter";
    }

}
