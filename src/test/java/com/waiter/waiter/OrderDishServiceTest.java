package com.waiter.waiter;


import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import com.waiter.waiter.entities.OrderDrink;
import com.waiter.waiter.repositories.OrderDishRepository;
import com.waiter.waiter.repositories.OrderDrinkRepository;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderDishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
////
    //    // when
    //    orderDishService.removeOneFromQuantity(orderDishId);
////
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

    //@Test
    //public void testAddOneToQuantity() {
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
    //    orderDishService.addOneToQuantity(orderDishId);
//
    //    // then
    //    verify(orderDishRepository).findById(orderDishId);
    //    verify(orderDishRepository).save(orderDish);
    //    verify(orderRepository).save(order);
    //    assertEquals(3, orderDish.getQuantity());
    //    assertEquals(30.0, orderDish.getCurrentPrice(), 0.01);
    //    assertEquals(30.0, order.getTotalCost(), 0.01);
    //}
//
    //@Test
    //public void testAddOneToQuantityWithNewOrderDish() {
    //    // given
    //    Integer orderDishId = 1;
    //    when(orderDishRepository.findById(orderDishId)).thenReturn(Optional.empty());
//
    //    // when
    //    orderDishService.addOneToQuantity(orderDishId);
//
    //    // then
    //    verify(orderDishRepository).findById(orderDishId);
    //    ArgumentCaptor<OrderDish> orderDishCaptor = ArgumentCaptor.forClass(OrderDish.class);
    //    verify(orderDishRepository).save(orderDishCaptor.capture());
    //    OrderDish savedOrderDish = orderDishCaptor.getValue();
    //    assertEquals(orderDishId, savedOrderDish.getId());
    //    assertEquals(1, savedOrderDish.getQuantity());
    //    assertEquals(0.0, savedOrderDish.getCurrentPrice(), 0.01);
//
    //    ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
    //    verify(orderRepository).save(orderCaptor.capture());
    //    Order savedOrder = orderCaptor.getValue();
    //    assertEquals(0.0, savedOrder.getTotalCost(), 0.01);
    //}

    @Test
    public void testGetOrderDishById_WhenOrderDishExists_ThenReturnOrderDish() {
        // Arrange
        Integer orderDishId = 1;
        OrderDish expectedOrderDish = new OrderDish();
        expectedOrderDish.setId(orderDishId);
        when(orderDishRepository.findById(orderDishId)).thenReturn(Optional.of(expectedOrderDish));

        // Act
        OrderDish actualOrderDish = orderDishService.getOrderDishById(orderDishId);

        // Assert
        assertEquals(expectedOrderDish, actualOrderDish);
    }

    @Test
    public void testGetOrderDishById_WhenOrderDishDoesNotExist_ThenReturnEmptyOrderDish() {
        Integer orderDishId = 1;
        when(orderDishRepository.findById(orderDishId)).thenReturn(Optional.empty());

        OrderDish actualOrderDish = orderDishService.getOrderDishById(orderDishId);

        assertNotNull(actualOrderDish);
        assertNull(actualOrderDish.getId());
        assertNull(actualOrderDish.getOrder());
        assertEquals(0, actualOrderDish.getPricePerItem());
        assertEquals(0, actualOrderDish.getPricePerItem());
    }

    @Test
    void testFindAllNotAddedDishesToOrder_NoDishes() {
        Order order = new Order();
        when(orderDishRepository.getAllNotAddedDishesToOrder(order)).thenReturn(Collections.emptyList());
        Iterable<Dish> actualDishes = orderDishService.findAllNotAddedDishesToOrder(order);
        assertNotNull(actualDishes);
        assertFalse(actualDishes.iterator().hasNext());
    }
    @Test
    void testFindAllNotAddedDishesToOrder_CallsRepositoryWithCorrectOrder() {
        Order order = new Order();
        orderDishService.findAllNotAddedDishesToOrder(order);
        verify(orderDishRepository).getAllNotAddedDishesToOrder(order);
    }
    @Test
    void testFindAllNotAddedDishesToOrder_ReturnsCorrectDishes() {
        // Arrange
        Order order = new Order();
        Dish dish1 = new Dish();
        Dish dish2 = new Dish();
        List<Dish> dishes = Arrays.asList(dish1, dish2);
        when(orderDishRepository.getAllNotAddedDishesToOrder(order)).thenReturn(dishes);

        Iterable<Dish> actualDishes = orderDishService.findAllNotAddedDishesToOrder(order);

        assertNotNull(actualDishes);
        Iterator<Dish> actualIterator = actualDishes.iterator();
        assertTrue(actualIterator.hasNext());
        assertEquals(dish1, actualIterator.next());
        assertTrue(actualIterator.hasNext());
        assertEquals(dish2, actualIterator.next());
        assertFalse(actualIterator.hasNext());
    }


    @Test
    public void testFindOrderIdByOrderDishId() {
        Integer orderDishId = 1;
        OrderDish orderDish = new OrderDish();
        orderDish.setId(orderDishId);
        Order order = new Order();
        order.setId(2);
        orderDish.setOrder(order);
        Optional<OrderDish> optionalOrderDish = Optional.of(orderDish);

        Mockito.when(orderDishRepository.findById(orderDishId)).thenReturn(optionalOrderDish);
        int orderId = orderDishService.findOrderIdByOrderDishId(orderDishId);

        assertEquals(2, orderId);
    }

    @Test
    public void testFindOrderIdByOrderDishIdWhenOrderDishNotFound() {
        Integer orderDishId = 1;
        Optional<OrderDish> optionalOrderDish = Optional.empty();

        Mockito.when(orderDishRepository.findById(orderDishId)).thenReturn(optionalOrderDish);
        int orderId = orderDishService.findOrderIdByOrderDishId(orderDishId);

        assertEquals(0, orderId);
    }

    @Test
    public void testGetOrderInfo() {
        Order order = new Order();
        order.setId(1);

        List<OrderDish> orderDishes = new ArrayList<>();
        OrderDish orderDish1 = new OrderDish();
        orderDish1.setId(1);
        orderDish1.setOrder(order);
        orderDishes.add(orderDish1);

        Mockito.when(orderDishRepository.getOrderInfo(order)).thenReturn(orderDishes);
        List<OrderDish> orderInfo = orderDishService.getOrderInfo(order);

        assertNotNull(orderInfo);
        assertEquals(1, orderInfo.size());
        assertEquals(1, orderInfo.get(0).getId());
        assertEquals(order, orderInfo.get(0).getOrder());
    }

    @Test
    public void testGetOrderInfoWhenNoOrderDishesFound() {
        Order order = new Order();
        order.setId(1);

        Mockito.when(orderDishRepository.getOrderInfo(order)).thenReturn(null);
        List<OrderDish> orderInfo = orderDishService.getOrderInfo(order);

        assertNotNull(orderInfo);
        assertEquals(0, orderInfo.size());
    }

}

