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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    DishRepository dishRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    RestaurantTablesRepository restaurantTablesRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    RestaurantTableService restaurantTableService;
    @Autowired
    OrderDishService orderDishService;
    @Autowired
    OrderDishRepository orderDishRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PostMapping("/create/{tId}")
    private String createOrder(@PathVariable(name = "tId") Integer tId, Model model) {
        Order order = new Order();
        RestaurantTable table = restaurantTableService.getTableById(tId);
        table.setHasOrder(true);
        restaurantTablesRepository.save(table);
        order.setTable(table);
        order.setCreatedOn(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.TAKING);


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepository.getUserByUsername(username);
        if (user.getRole() == Role.WAITER) {
            order.setWaiter(user);
        }

        orderRepository.save(order);
        //return "/orders/add-order";
        model.addAttribute(order);
        model.addAttribute("orderDish", orderDishService.getOrderInfo(order));
        boolean orderDishNull = false;
        if (orderDishService.getOrderInfo(order) == null) {
            orderDishNull = true;
        }
        model.addAttribute("orderDishNull", orderDishNull);
        return "/orders/view-order";
    }

    @PostMapping("/view/{tId}")
    private String viewOrderByTableId(@PathVariable(name = "tId") Integer tId, Model model) {
        Optional<RestaurantTable> t = restaurantTablesRepository.findById(tId);
        Order order = orderService.getOrderByTableId(t);
        model.addAttribute("order", order);

        model.addAttribute("orderDish", orderDishService.getOrderInfo(order));
        boolean orderDishNull = false;
        if (orderDishService.getOrderInfo(order).isEmpty()) {
            orderDishNull = true;
        }
        model.addAttribute("orderDishNull", orderDishNull);

        User user = userDetailsService.getLoggedUser();
        user.toString();


        boolean isWaiter = false;
        if (user.getRole() == Role.WAITER) {
            isWaiter = true;
        }
        boolean isAbleToChangeStatus = orderService.isAbleToChangeStatus(order.getOrderStatus(), isWaiter);
        model.addAttribute("isAbleToChangeStatus", isAbleToChangeStatus);
        model.addAttribute("user", user);


        return "/orders/view-order";
    }

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
        return "/orders/view-order";
    }

    @GetMapping
    public String getAllOrders(Model model) {
        Iterable<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "/orders/orders-list";
    }

    //<form th:action="@{/orders/delete-dishes/{orderId}(orderId=${order.id})}" method="post">
    //  <input type="image" src="https://www.freeiconspng.com/uploads/red-x-close-button-png-0.png" alt="Submit" width="40" height="40">
    //</form>
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
        return "/orders/view-order";
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
        return "/orders/view-order";
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
        return "/orders/view-order";
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
        return "/";
    }

}
