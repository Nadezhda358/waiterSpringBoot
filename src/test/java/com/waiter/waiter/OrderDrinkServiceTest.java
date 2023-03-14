package com.waiter.waiter;

import com.waiter.waiter.entities.*;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.repositories.OrderDishRepository;
import com.waiter.waiter.repositories.OrderDrinkRepository;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderDrinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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


    @Test
    public void testFindOrderIdByOrderDrinkId() {
        Integer orderDrinkId = 1;
        OrderDrink orderDrink = new OrderDrink();
        orderDrink.setId(orderDrinkId);
        Order order = new Order();
        order.setId(2);
        orderDrink.setOrder(order);
        Optional<OrderDrink> optionalOrderDrink = Optional.of(orderDrink);

        Mockito.when(orderDrinkRepository.findById(orderDrinkId)).thenReturn(optionalOrderDrink);
        int orderId = orderDrinkService.findOrderIdByOrderDrinkId(orderDrinkId);

        assertEquals(2, orderId);
    }

    @Test
    public void testFindOrderIdByOrderDrinkIdWhenOrderDrinkNotFound() {
        Integer orderDrinkId = 1;
        Optional<OrderDrink> optionalOrderDrink = Optional.empty();

        Mockito.when(orderDrinkRepository.findById(orderDrinkId)).thenReturn(optionalOrderDrink);
        int orderId = orderDrinkService.findOrderIdByOrderDrinkId(orderDrinkId);

        assertEquals(0, orderId);
    }

    @Test
    public void testGetOrderInfo() {
        Order order = new Order();
        order.setId(1);

        List<OrderDrink> orderDrinks = new ArrayList<>();
        OrderDrink orderDrink1 = new OrderDrink();
        orderDrink1.setId(1);
        orderDrink1.setOrder(order);
        orderDrinks.add(orderDrink1);

        Mockito.when(orderDrinkRepository.getOrderInfo(order)).thenReturn(orderDrinks);
        List<OrderDrink> orderInfo = orderDrinkService.getOrderInfo(order);

        assertNotNull(orderInfo);
        assertEquals(1, orderInfo.size());
        assertEquals(1, orderInfo.get(0).getId());
        assertEquals(order, orderInfo.get(0).getOrder());
    }

    @Test
    public void testGetOrderInfoWhenNoOrderDrinksFound() {
        Order order = new Order();
        order.setId(1);

        Mockito.when(orderDrinkRepository.getOrderInfo(order)).thenReturn(null);
        List<OrderDrink> orderInfo = orderDrinkService.getOrderInfo(order);

        assertNotNull(orderInfo);
        assertEquals(0, orderInfo.size());
    }

    //@Test
    //public void testDeleteOrderDrinkByIdWhenOrderDrinkCanBeDeleted() {
    //    // given
    //    Integer orderDrinkId = 1;
    //    int orderId = 2;
//
    //    OrderDrink orderDrink = new OrderDrink();
    //    Order order = new Order();
    //    order.setId(orderId);
    //    order.setOrderStatus(OrderStatus.TAKING);
    //    orderDrink.setOrder(order);
//
    //    Mockito.when(orderDrinkRepository.findById(orderDrinkId)).thenReturn(Optional.of(orderDrink));
    //    Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
//
    //    // when
    //    orderDrinkService.deleteOrderDrinkById(orderDrinkId, orderId);
//
    //    // then
    //    Mockito.verify(orderDrinkRepository, Mockito.times(1)).deleteById(orderDrinkId);
    //    Mockito.verify(orderRepository, Mockito.times(1)).save(order);
    //    Mockito.verify(orderDrinkService, Mockito.times(1)).updateTotalCostOrder(order);
    //}
//
    //@Test
    //public void testDeleteOrderDrinkByIdWhenOrderDrinkCannotBeDeleted() {
    //    // given
    //    Integer orderDrinkId = 1;
    //    int orderId = 2;
//
    //    OrderDrink orderDrink = new OrderDrink();
    //    Order order = new Order();
    //    order.setId(orderId);
    //    order.setOrderStatus(OrderStatus.SERVED);
    //    orderDrink.setOrder(order);
//
    //    Mockito.when(orderDrinkRepository.findById(orderDrinkId)).thenReturn(Optional.of(orderDrink));
//
    //    // when
    //    orderDrinkService.deleteOrderDrinkById(orderDrinkId, orderId);
//
    //    // then
    //    Mockito.verify(orderDrinkRepository, Mockito.times(0)).deleteById(orderDrinkId);
    //    Mockito.verify(orderRepository, Mockito.times(0)).save(order);
    //    Mockito.verify(orderDrinkService, Mockito.times(0)).updateTotalCostOrder(order);
    //}

    //@Test
    //public void testDeleteOrderDrinkById() {
    //    // Create an OrderDrink object and save it to the database
    //    OrderDrink orderDrink = new OrderDrink();
    //    orderDrink.setOrder(new Order());
    //    orderDrink.getOrder().setOrderStatus(OrderStatus.TAKING);
//
    //    OrderDrink savedOrderDrink = orderDrinkRepository.save(orderDrink);
    //    assertNotNull(savedOrderDrink);
//
    //    // Call the deleteOrderDrinkById method with the OrderDrink ID and an Order ID
    //    orderDrinkService.deleteOrderDrinkById(savedOrderDrink.getId(), savedOrderDrink.getOrder().getId());
//
    //    // Verify that the OrderDrink has been deleted
    //    assertFalse(orderDrinkRepository.existsById(savedOrderDrink.getId()));
//
    //    // Verify that the Order's total cost has been updated
    //    Optional<Order> order = orderRepository.findById(savedOrderDrink.getOrder().getId());
    //    assertTrue(order.isPresent());
    //    assertEquals(0, order.get().getTotalCost());
    //}
//
//
    //@Test
    //public void testDeleteOrderDrinkByIdWithInvalidOrderDrinkId() {
    //    // Call the deleteOrderDrinkById method with an invalid OrderDrink ID and a valid Order ID
    //    orderDrinkService.deleteOrderDrinkById(12345, 1);
//
    //    // Verify that no exceptions were thrown
    //}
//
    //@Test
    //public void testDeleteOrderDrinkByIdWithOrderInProgress() {
    //    // Create an OrderDrink object and save it to the database
    //    OrderDrink orderDrink = new OrderDrink();
    //    orderDrink.setOrder(new Order());
    //    orderDrink.getOrder().setOrderStatus(OrderStatus.COOKING);
    //    orderDrink = orderDrinkRepository.save(orderDrink);
//
    //    // Call the deleteOrderDrinkById method with the OrderDrink ID and an Order ID
    //    orderDrinkService.deleteOrderDrinkById(orderDrink.getId(), orderDrink.getOrder().getId());
//
    //    // Verify that the OrderDrink has not been deleted
    //    assertTrue(orderDrinkRepository.existsById(orderDrink.getId()));
//
    //    // Verify that the Order's total cost has not been updated
    //    Optional<Order> order = orderRepository.findById(orderDrink.getOrder().getId());
    //    assertTrue(order.isPresent());
    //    assertNotEquals(0, order.get().getTotalCost());
    //}



}

