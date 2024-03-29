package com.waiter.waiter;

import com.waiter.waiter.entities.*;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.helpingClasses.OrderDishHelp;
import com.waiter.waiter.helpingClasses.OrderDrinkHelp;
import com.waiter.waiter.repositories.OrderDishRepository;
import com.waiter.waiter.repositories.OrderDrinkRepository;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderDrinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

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
    @Mock
    private Model model;

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


    @Test
    public void prepareToAddDrinks_ShouldPopulateModelAttributes() {
        Integer orderId = 123;
        Order order = new Order();
        order.setId(orderId);

        Iterable<Drink> drinks = Arrays.asList(new Drink(), new Drink());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderDrinkRepository.getAllNotAddedDrinksToOrder(order)).thenReturn((List<Drink>) drinks);

        orderDrinkService.prepareToAddDrinks(orderId, model);

        ArgumentCaptor<OrderDrinkHelp> orderDrinkHelpCaptor = ArgumentCaptor.forClass(OrderDrinkHelp.class);
        verify(model).addAttribute(eq("orderdrink"), orderDrinkHelpCaptor.capture());
        assertEquals(order, orderDrinkHelpCaptor.getValue().getOrder());

        verify(model).addAttribute(eq("selectableDrinks"), eq(drinks));
        verify(model).addAttribute(eq("order"), eq(order));
    }

    @Test
    public void prepareToAddDrinks_WithInvalidOrderId_ShouldThrowException() {
        Integer orderId = 123;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        try {
            orderDrinkService.prepareToAddDrinks(orderId, model);
            fail("Expected NoSuchElementException was not thrown");
        } catch (NoSuchElementException e) {
            assertEquals("No value present", e.getMessage());
        }
    }

    @Test
    public void testSaveDrinksToOrderWithNullDrinks() {
        Order order = new Order();
        OrderDrinkHelp orderDrinkHelp = new OrderDrinkHelp(order);
        orderDrinkHelp.setDrinks(null);
        when(orderDrinkRepository.save(any(OrderDrink.class))).thenReturn(new OrderDrink());
        orderDrinkService.saveDrinksToOrder(orderDrinkHelp);
        verify(orderDrinkRepository, times(0)).save(any(OrderDrink.class));
    }
    @Test
    public void testSaveDrinksToOrder() {
        Order order = new Order();
        Drink drink1 = new Drink();
        Drink drink2 = new Drink();

        OrderDrinkHelp orderDrinkHelp = new OrderDrinkHelp(order);
        orderDrinkHelp.setDrinks(Arrays.asList(drink1, drink2));

        when(orderDrinkRepository.save(any(OrderDrink.class))).thenReturn(new OrderDrink());
        orderDrinkService.saveDrinksToOrder(orderDrinkHelp);

        verify(orderDrinkRepository, times(2)).save(any(OrderDrink.class));
    }

    @Test
    public void testAddOneToQuantityExistingOrderDrink() {
        Integer orderDrinkId = 1;
        OrderDrink orderDrink = new OrderDrink();
        orderDrink.setQuantity(2);
        orderDrink.setPricePerItem(10.0);
        Order order = new Order();
        order.setId(1);
        orderDrink.setOrder(order);
        when(orderDrinkRepository.findById(orderDrinkId)).thenReturn(Optional.of(orderDrink));

        orderDrinkService.addOneToQuantity(orderDrinkId);

        verify(orderDrinkRepository).findById(orderDrinkId);
        verify(orderDrinkRepository).save(orderDrink);
        assertEquals(Integer.valueOf(3), orderDrink.getQuantity());
        assertEquals(Double.valueOf(30.0), orderDrink.getCurrentPrice());
    }




}

