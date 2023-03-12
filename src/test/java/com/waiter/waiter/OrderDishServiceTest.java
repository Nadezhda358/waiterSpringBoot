package com.waiter.waiter;


import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import com.waiter.waiter.repositories.OrderDishRepository;
import com.waiter.waiter.repositories.OrderDrinkRepository;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderDishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderDishServiceTest {
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Mock
    private OrderDishRepository orderDishRepository;

    @Mock
    private OrderDrinkRepository orderDrinkRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderDishService orderDishService;

    @Test
    public void updateTotalCostOrder_shouldNotUpdateTotalCostIfNoTotalCost() {
        // given
        Order order = new Order();
        order.setId(1);

        when(orderDishRepository.getTotalCost(order)).thenReturn(Optional.empty());
        when(orderDrinkRepository.getTotalCost(order)).thenReturn(Optional.empty());

        // when
        orderDishService.updateTotalCostOrder(order);

        // then
        verify(orderDishRepository).getTotalCost(order);
        verify(orderDrinkRepository).getTotalCost(order);
        verify(orderRepository).save(order);

        assertEquals(0.0, order.getTotalCost(), 0.01);
    }

    @Test
    public void testUpdateTotalCostOrder_WithTotalCost() {
        Order order = new Order();
        order.setId(1);

        double dishTotalCost = 50.0;
        when(orderDishRepository.getTotalCost(order)).thenReturn(Optional.of(dishTotalCost));

        double drinkTotalCost = 25.0;
        when(orderDrinkRepository.getTotalCost(order)).thenReturn(Optional.of(drinkTotalCost));

        orderDishService.updateTotalCostOrder(order);

        verify(orderDishRepository, atLeastOnce()).getTotalCost(order);
        verify(orderDrinkRepository, atLeastOnce()).getTotalCost(order);
        verify(orderRepository).save(order);

        double expectedTotalCost = dishTotalCost + drinkTotalCost;
        assertEquals(expectedTotalCost, order.getTotalCost(), 0.01);
    }

    @Test
    public void testUpdateTotalCostOrder_WithoutTotalCost() {
        Order order = new Order();
        order.setId(1);

        when(orderDishRepository.getTotalCost(order)).thenReturn(Optional.empty());
        when(orderDrinkRepository.getTotalCost(order)).thenReturn(Optional.empty());

        orderDishService.updateTotalCostOrder(order);

        verify(orderDishRepository).getTotalCost(order);
        verify(orderDrinkRepository).getTotalCost(order);
        verify(orderRepository).save(order);

        assertEquals(0.0, order.getTotalCost(), 0.01);
    }


    //@Test
    //public void testRemoveOneFromQuantity() {
    //    // given
    //    Integer orderDishId = 1;
    //    OrderDish orderDish = new OrderDish();
    //    orderDish.setId(orderDishId);
    //    orderDish.setQuantity(2);
    //    orderDish.setPricePerItem(10.0);
    //    orderDish.setCurrentPrice(20.0);
    //    Order order = new Order();
    //    order.setId(1);
    //    order.setTotalCost(20.0);
    //    orderDish.setOrder(order);
    //    when(orderDishRepository.findById(orderDishId)).thenReturn(Optional.of(orderDish));
//
    //    // when
    //    orderDishService.removeOneFromQuantity(orderDishId);
//
    //    // then
    //    verify(orderDishRepository).findById(orderDishId);
    //    verify(orderDishRepository).save(orderDish);
    //    verify(orderRepository).save(order);
    //    assertEquals(1, orderDish.getQuantity());
    //    assertEquals(10.0, orderDish.getCurrentPrice(), 0.01);
    //    assertEquals(10.0, order.getTotalCost(), 0.01);
    //}


    @Test
    public void removeOneFromQuantity_shouldNotUpdateOrderDishAndOrderIfQuantityIsOne() {
        // given
        Integer orderDishId = 1;
        OrderDish orderDish = new OrderDish();
        orderDish.setId(orderDishId);
        orderDish.setQuantity(1);
        Order order = new Order();
        order.setId(1);
        orderDish.setOrder(order);
        when(orderDishRepository.findById(orderDishId)).thenReturn(Optional.of(orderDish));

        // when
        orderDishService.removeOneFromQuantity(orderDishId);

        // then
        verify(orderDishRepository).findById(orderDishId);
        verifyNoMoreInteractions(orderDishRepository);
        verifyNoMoreInteractions(orderRepository);
        assertEquals(1, orderDish.getQuantity());
        assertEquals(orderDish.getCurrentPrice(), 0.0);
        assertEquals(order.getTotalCost(), 0.0);
    }

    @Test
    public void removeOneFromQuantity_shouldNotUpdateOrderDishAndOrderIfOrderDishNotFound() {
        // given
        Integer orderDishId = 1;
        when(orderDishRepository.findById(orderDishId)).thenReturn(Optional.empty());

        // when
        orderDishService.removeOneFromQuantity(orderDishId);

        // then
        verify(orderDishRepository).findById(orderDishId);
        verifyNoMoreInteractions(orderDishRepository);
        verifyNoMoreInteractions(orderRepository);
    }

}

