package com.waiter.waiter.controllers;

import com.waiter.waiter.entities.*;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.services.OrderService;
import com.waiter.waiter.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PostMapping("/create/{tId}")
    private String createOrder(@PathVariable(name = "tId") Integer tId, Model model) {
       orderService.createOrder(tId);
        return "redirect:/orders/view/"+tId;
    }
    @GetMapping("/view/{tId}")
    private String viewOrderByTableId1(@PathVariable(name = "tId") Integer tId, Model model) {
        orderService.viewOrderByTableId(tId, model);
        return "/orders/view-order";
    }
    @PostMapping("/view/{tId}")
    private String viewOrderByTableId2(@PathVariable(name = "tId") Integer tId, Model model) {
        orderService.viewOrderByTableId(tId, model);
        return "/orders/view-order";
    }
    @PostMapping("/view-more/{tId}")
    private String viewOrderByTableId3(@PathVariable(name = "tId") Integer tId, Model model) {
        orderService.viewMoreAboutOrderByTableId(tId, model);
        return "/orders/view-order";
    }
    @PostMapping("/change-status/{orderId}")
    public String changeStatus(@PathVariable(name = "orderId") Integer orderId, Model model) {
        Order order=orderService.getOrderById(orderId);
        orderService.ChangeStatus(order);
        if(order.getOrderStatus()==OrderStatus.PAID){
            return "redirect:/tables";
        }
        int tId=order.getTable().getId();
        return "redirect:/orders/view/"+tId;
    }
    @GetMapping("/reference-waiter")
    public String getOrdersReferenceWaiter(Model model, @RequestParam(required = false, defaultValue = "your") String filter1, @RequestParam(required = false, defaultValue = "newest") String filter2) {
        Iterable<Order> orders = orderService.getOrdersForWaiterByDate(filter1, filter2);
        model.addAttribute("orders", orders);

        model.addAttribute("loggedUser", userDetailsService.getLoggedUser());
        model.addAttribute("filter1", filter1);
        model.addAttribute("filter2", filter2);
        return "/orders/orders-reference-waiter";
    }
    @GetMapping("/reference-cook")
    public String getOrderReferenceCook(Model model, @RequestParam(required = false, defaultValue = "your") String filter){
        Iterable<Object[]> ordersCountByDate = orderService.getOrdersForCookByDate(filter);
        model.addAttribute("ordersCountByDate", ordersCountByDate);

        model.addAttribute("loggedUser", userDetailsService.getLoggedUser());
        model.addAttribute("filter", filter);
        return "/orders/orders-reference-cook";
    }
    @GetMapping("/orders-for-date")
    public String showOrdersForDate(@RequestParam(name = "date") String dateString, @RequestParam(required = false, defaultValue = "your") String filter, Model model){
        Iterable<Order> orders = orderService.getOrdersForCertainDate(dateString, filter);
        model.addAttribute("orders", orders);
        return "/orders/orders-for-date";
    }
    @GetMapping("/active")
    public String getActiveOrders(@RequestParam(required = false, defaultValue = "your") String filter, Model model) {
        Iterable<Order> orders = orderService.getActiveOrders(filter);
        model.addAttribute("orders", orders);

        model.addAttribute("loggedUser", userDetailsService.getLoggedUser());
        model.addAttribute("filter", filter);

        return "/orders/orders-list";
    }
}
