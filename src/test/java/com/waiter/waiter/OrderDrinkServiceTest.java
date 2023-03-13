package com.waiter.waiter;

import com.waiter.waiter.entities.*;
import com.waiter.waiter.repositories.OrderDishRepository;
import com.waiter.waiter.repositories.OrderDrinkRepository;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderDrinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

public class OrderDrinkServiceTest {
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private OrderDrinkRepository orderDrinkRepository;

    @Mock
    private OrderDishRepository orderDishRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderDrinkService orderDrinkService;

    @Test
    public void updateTotalCostOrder_shouldNotUpdateTotalCostIfNoTotalCost() {
        Order order = new Order();
        order.setId(1);
        when(orderDishRepository.getTotalCost(order)).thenReturn(Optional.empty());
        when(orderDrinkRepository.getTotalCost(order)).thenReturn(Optional.empty());
        orderDrinkService.updateTotalCostOrder(order);
        verify(orderDrinkRepository).getTotalCost(order);
        verify(orderDishRepository).getTotalCost(order);
        verify(orderRepository).save(order);
        assertEquals(0.0, order.getTotalCost(), 0.01);
    }

    @Test
    public void testUpdateTotalCostOrder_WithTotalCost() {
        Order order = new Order();
        order.setId(1);

        double drinkTotalCost = 50.0;
        when(orderDrinkRepository.getTotalCost(order)).thenReturn(Optional.of(drinkTotalCost));

        double dishTotalCost = 25.0;
        when(orderDishRepository.getTotalCost(order)).thenReturn(Optional.of(dishTotalCost));

        orderDrinkService.updateTotalCostOrder(order);

        verify(orderDrinkRepository, atLeastOnce()).getTotalCost(order);
        verify(orderDishRepository, atLeastOnce()).getTotalCost(order);
        verify(orderRepository).save(order);

        double expectedTotalCost = dishTotalCost + drinkTotalCost;
        assertEquals(expectedTotalCost, order.getTotalCost(), 0.01);
    }


    @Test
    public void removeOneFromQuantity_shouldNotUpdateOrderDishAndOrderIfQuantityIsOne() {
        Integer orderDrinkId = 1;
        OrderDrink orderDrink = new OrderDrink();
        orderDrink.setId(orderDrinkId);
        orderDrink.setQuantity(1);
        Order order = new Order();
        order.setId(1);
        orderDrink.setOrder(order);
        when(orderDrinkRepository.findById(orderDrinkId)).thenReturn(Optional.of(orderDrink));

        orderDrinkService.removeOneFromQuantity(orderDrinkId);

        verify(orderDrinkRepository).findById(orderDrinkId);
        verifyNoMoreInteractions(orderDrinkRepository);
        verifyNoMoreInteractions(orderRepository);
        assertEquals(1, orderDrink.getQuantity());
        assertEquals(orderDrink.getCurrentPrice(), 0.0);
        assertEquals(order.getTotalCost(), 0.0);
    }

    @Test
    public void removeOneFromQuantity_shouldNotUpdateOrderDrinkAndOrderIfOrderDrinkNotFound() {
        Integer orderDrinkId = 1;
        when(orderDrinkRepository.findById(orderDrinkId)).thenReturn(Optional.empty());

        orderDrinkService.removeOneFromQuantity(orderDrinkId);

        verify(orderDrinkRepository).findById(orderDrinkId);
        verifyNoMoreInteractions(orderDrinkRepository);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void testGetOrderDrinkById_WhenOrderDrinkExists_ThenReturnOrderDrink() {
        Integer orderDrinkId = 1;
        OrderDrink expectedOrderDrink = new OrderDrink();
        expectedOrderDrink.setId(orderDrinkId);
        when(orderDrinkRepository.findById(orderDrinkId)).thenReturn(Optional.of(expectedOrderDrink));

        OrderDrink actualOrderDrink = orderDrinkService.getOrderDrinkById(orderDrinkId);

        assertEquals(expectedOrderDrink, actualOrderDrink);
    }

    @Test
    public void testGetOrderDrinkById_WhenOrderDrinkDoesNotExist_ThenReturnEmptyOrderDrink() {
        Integer orderDrinkId = 1;
        when(orderDrinkRepository.findById(orderDrinkId)).thenReturn(Optional.empty());

        OrderDrink actualOrderDrink = orderDrinkService.getOrderDrinkById(orderDrinkId);

        assertNotNull(actualOrderDrink);
        assertNull(actualOrderDrink.getId());
        assertNull(actualOrderDrink.getOrder());
        assertEquals(0, actualOrderDrink.getPricePerItem());
        assertEquals(0, actualOrderDrink.getPricePerItem());
    }

    @Test
    void testFindAllNotAddedDrinksToOrder_NoDrinks() {
        Order order = new Order();
        when(orderDrinkRepository.getAllNotAddedDrinksToOrder(order)).thenReturn(Collections.emptyList());
        Iterable<Drink> actualDrinks = orderDrinkService.findAllNotAddedDrinksToOrder(order);
        assertNotNull(actualDrinks);
        assertFalse(actualDrinks.iterator().hasNext());
    }

    @Test
    void testFindAllNotAddedDrinksToOrder_CallsRepositoryWithCorrectOrder() {
        Order order = new Order();
        orderDrinkService.findAllNotAddedDrinksToOrder(order);
        verify(orderDrinkRepository).getAllNotAddedDrinksToOrder(order);
    }

    @Test
    void testFindAllNotAddedDrinksToOrder_ReturnsCorrectDrinks() {
        // Arrange
        Order order = new Order();
        Drink drink1 = new Drink();
        Drink drink2 = new Drink();
        List<Drink> drinks = Arrays.asList(drink1, drink2);
        when(orderDrinkRepository.getAllNotAddedDrinksToOrder(order)).thenReturn(drinks);

        Iterable<Drink> actualDrinks = orderDrinkService.findAllNotAddedDrinksToOrder(order);

        assertNotNull(actualDrinks);
        Iterator<Drink> actualIterator = actualDrinks.iterator();
        assertTrue(actualIterator.hasNext());
        assertEquals(drink1, actualIterator.next());
        assertTrue(actualIterator.hasNext());
        assertEquals(drink2, actualIterator.next());
        assertFalse(actualIterator.hasNext());
    }
}
