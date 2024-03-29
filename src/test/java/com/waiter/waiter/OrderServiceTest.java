package com.waiter.waiter;

import com.waiter.waiter.entities.*;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.enums.Role;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import com.waiter.waiter.repositories.UserRepository;
import com.waiter.waiter.services.OrderService;
import com.waiter.waiter.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private RestaurantTablesRepository restaurantTablesRepository;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrderByTableId() {
        RestaurantTable table = new RestaurantTable();
        Order expectedOrder = new Order();
        when(orderRepository.getOrderByTableId(Optional.of(table))).thenReturn(expectedOrder);
        Order actualOrder = orderService.getOrderByTableId(Optional.of(table));
        assertThat(actualOrder, is(expectedOrder));
    }

    @Test
    public void testGetOrderByTableIdWithEmptyTableId() {
        when(orderRepository.getOrderByTableId(Optional.empty())).thenReturn(null);
        Order actualOrder = orderService.getOrderByTableId(Optional.empty());
        assertThat(actualOrder, is(nullValue()));
    }

    @Test
    public void testIsOrderDrinkNullWithEmptyList() {
        List<OrderDrink> orderDrink = new ArrayList<>();
        boolean result = orderService.isOrderDrinkNull(orderDrink);
        assertTrue(result);
    }

    @Test
    public void testIsOrderDrinkNullWithNonEmptyList() {
        List<OrderDrink> orderDrink = new ArrayList<>();
        orderDrink.add(new OrderDrink());
        boolean result = orderService.isOrderDrinkNull(orderDrink);
        assertFalse(result);
    }

    @Test
    public void testIsOrderDishNullWithEmptyList() {
        List<OrderDish> orderDish = new ArrayList<>();
        boolean result = orderService.isOrderDishNull(orderDish);
        assertTrue(result);
    }

    @Test
    public void testIsOrderDishNullWithNonEmptyList() {
        List<OrderDish> orderDish = new ArrayList<>();
        orderDish.add(new OrderDish());
        boolean result = orderService.isOrderDishNull(orderDish);
        assertFalse(result);
    }

    @Test
    public void testGetOrderByIdWithValidId() {
        int orderId = 1;
        Order expectedOrder = new Order();
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        Order actualOrder = orderService.getOrderById(orderId);

        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testGetOrderByIdWithInvalidId() {
        int orderId = 2;
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Order actualOrder = orderService.getOrderById(orderId);

        Assertions.assertNotNull(actualOrder);
        assertEquals(null, actualOrder.getId());
        Assertions.assertNull(actualOrder.getCook());
    }

    @Test
    public void testGetOrdersForCertainDateWithAllFilter() {
        String date = "2022-03-15";
        String filter = "all";
        Order order1 = new Order();
        order1.setId(1);
        Order order2 = new Order();
        order1.setId(2);
        List<Order> expectedOrders = Arrays.asList(order1, order2);
        Mockito.when(orderRepository.getAllOrdersForCertainDate(date)).thenReturn(expectedOrders);

        Iterable<Order> actualOrders = orderService.getOrdersForCertainDate(date, filter);

        assertEquals(expectedOrders, actualOrders);
    }
    @Test
    public void testGetOrdersForCertainDateWithInvalidFilter() {
        String date = "2022-03-15";
        String filter = "invalid";
        Iterable<Order> expectedOrders = new ArrayList<>();
        Mockito.when(orderRepository.getAllOrdersForCertainDate(date)).thenReturn(expectedOrders);

        Iterable<Order> actualOrders = orderService.getOrdersForCertainDate(date, filter);

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void getOrdersForCookByDate_shouldReturnAllOrdersCountByDateForFilterAll() {
        // Arrange
        String filter1 = "all";
        Iterable<Object[]> expectedOrders = Arrays.asList(new Object[][]{
                {"2023-03-12", 2},
                {"2023-03-11", 1},
                {"2023-03-10", 1}
        });
        when(orderRepository.getOrdersCountByDate()).thenReturn(expectedOrders);

        Iterable<Object[]> actualOrders = orderService.getOrdersForCookByDate(filter1);

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void testGetOrdersForCookByDate_all() {
        List<Object[]> orderData = new ArrayList<>();
        orderData.add(new Object[]{"2023-03-12", 2L});
        orderData.add(new Object[]{"2023-03-11", 1L});
        Mockito.when(orderRepository.getOrdersCountByDate()).thenReturn(orderData);

        Iterable<Object[]> orders = orderService.getOrdersForCookByDate("all");

        assertEquals(orderData, orders);
    }

    @Test
    public void testGetOrdersForCookByDate_invalidFilter() {
        Iterable<Object[]> orders = orderService.getOrdersForCookByDate("invalid");

        assertEquals(new ArrayList<>(), orders);
    }

    @Test
    public void testGetOrdersForWaiterByDate_all_newest() {
        Order order1 = new Order();
        order1.setId(1);
        order1.setCreatedOn(LocalDateTime.parse("2023-03-13T10:00:00"));
        Order order2 = new Order();
        order2.setId(2);
        order2.setCreatedOn(LocalDateTime.parse("2023-03-12T10:00:00"));
        Order order3 = new Order();
        order3.setId(3);
        order3.setCreatedOn(LocalDateTime.parse("2023-03-11T10:00:00"));
        List<Order> orderData = Arrays.asList(order1, order2, order3);
        Mockito.when(orderRepository.findAll()).thenReturn(orderData);

        Iterable<Order> orders = orderService.getOrdersForWaiterByDate("all", "newest");

        assertEquals(orderData, orders);
    }

    @Test
    public void testGetOrdersForWaiterByDate_all_oldest() {
        Order order1 = new Order();
        order1.setId(1);
        order1.setCreatedOn(LocalDateTime.parse("2023-03-13T10:00:00"));
        Order order2 = new Order();
        order2.setId(2);
        order2.setCreatedOn(LocalDateTime.parse("2023-03-12T10:00:00"));
        Order order3 = new Order();
        order3.setId(3);
        order3.setCreatedOn(LocalDateTime.parse("2023-03-11T10:00:00"));

        List<Order> orderData = Arrays.asList(order3, order2, order1);
        Mockito.when(orderRepository.findAll()).thenReturn(orderData);

        Iterable<Order> orders = orderService.getOrdersForWaiterByDate("all", "oldest");

        assertEquals(orderData, orders);
    }

    @Test
    public void testGetActiveOrdersForWaiter_your() {
        UserDetailsServiceImpl userDetailsService = Mockito.mock(UserDetailsServiceImpl.class);
        Mockito.when(userDetailsService.getLoggedUser()).thenReturn(new User());

        Order order1 = new Order();
        order1.setId(1);
        order1.setOrderStatus(OrderStatus.TAKEN);
        Order order2 = new Order();
        order2.setId(2);
        order2.setOrderStatus(OrderStatus.COOKING);
        Order order3 = new Order();
        order3.setId(3);
        order3.setOrderStatus(OrderStatus.PAID);
        List<Order> orderData = Arrays.asList(order1, order2, order3);
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        Mockito.when(orderRepository.getActiveOrdersForCertainWaiter(Mockito.anyInt())).thenReturn(orderData);

    }

    @Test
        void testIsAbleToChangeStatus_WaiterTaking() {
            User waiter = new User();
            waiter.setRole(Role.WAITER);
            assertTrue(orderService.isAbleToChangeStatus(OrderStatus.TAKING, waiter, null, waiter));
        }

        @Test
        void testIsAbleToChangeStatus_WaiterCooked() {
            User waiter = new User();
            waiter.setRole(Role.WAITER);
            assertTrue(orderService.isAbleToChangeStatus(OrderStatus.COOKED, waiter, null, waiter));
        }

        @Test
        void testIsAbleToChangeStatus_WaiterServed() {
            User waiter = new User();
            waiter.setRole(Role.WAITER);
            assertTrue(orderService.isAbleToChangeStatus(OrderStatus.SERVED, waiter, null, waiter));
        }

        @Test
        void testIsAbleToChangeStatus_CookTaken() {
            User cook = new User();
            cook.setRole(Role.COOK);
            assertTrue(orderService.isAbleToChangeStatus(OrderStatus.TAKEN, null, cook, cook));
        }

        @Test
        void testIsAbleToChangeStatus_CookCookingWithSameCook() {
            User cook = new User();
            cook.setRole(Role.COOK);
            assertTrue(orderService.isAbleToChangeStatus(OrderStatus.COOKING, null, cook, cook));
        }

        @Test
        void testIsAbleToChangeStatus_CookCookingWithDifferentCook() {
            User cook = new User();
            cook.setRole(Role.COOK);
            User otherCook = new User();
            cook.setRole(Role.COOK);
            assertFalse(orderService.isAbleToChangeStatus(OrderStatus.COOKING, null, otherCook, cook));
        }

        @Test
        void testIsAbleToChangeStatus_InvalidStatus() {
            User waiter = new User();
            waiter.setRole(Role.WAITER);
            User cook = new User();
            cook.setRole(Role.COOK);
            assertFalse(orderService.isAbleToChangeStatus(OrderStatus.COOKING, waiter, cook, waiter));
        }


    @Test
    void testIsAbleToChangeStatus_InvalidWaiter_ReturnsFalse() {
        OrderStatus validStatus = OrderStatus.TAKING;
        User invalidWaiter = new User();
        User validCook = new User();
        User validLoggedUser = new User();
        validLoggedUser.setRole(Role.WAITER);
        validLoggedUser.setId(1);
        invalidWaiter.setId(2);

        OrderService orderService = new OrderService();

        boolean result = orderService.isAbleToChangeStatus(validStatus, invalidWaiter, validCook, validLoggedUser);

        assertFalse(result);
    }

    @Test
    public void testGetActiveOrdersForWaiterWithAllFilter() {
        User waiter = new User();
        waiter.setRole(Role.WAITER);
        Mockito.when(userDetailsService.getLoggedUser()).thenReturn(waiter);

        Iterable<Order> expectedOrders = Arrays.asList(new Order(), new Order(), new Order());
        Mockito.when(orderRepository.getAllActiveOrdersForWaiter()).thenReturn(expectedOrders);

        Iterable<Order> actualOrders = orderService.getActiveOrders("all");

        assertEquals(expectedOrders, actualOrders);
    }


    @Test
    public void testGetActiveOrdersForWaiterWithYourFilter() {
        User waiter = new User();
        waiter.setId(1);
        waiter.setRole(Role.WAITER);
        Mockito.when(userDetailsService.getLoggedUser()).thenReturn(waiter);

        Iterable<Order> expectedOrders = Arrays.asList(new Order(), new Order());
        Mockito.when(orderRepository.getActiveOrdersForCertainWaiter(1)).thenReturn(expectedOrders);

        Iterable<Order> actualOrders = orderService.getActiveOrders("your");

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void testGetActiveOrdersForCookWithAllFilter() {
        User cook = new User();
        cook.setRole(Role.COOK);
        Mockito.when(userDetailsService.getLoggedUser()).thenReturn(cook);

        Iterable<Order> expectedOrders = Arrays.asList(new Order(), new Order());
        Mockito.when(orderRepository.getAllActiveOrdersForCook()).thenReturn(expectedOrders);

        Iterable<Order> actualOrders = orderService.getActiveOrders("all");

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void testGetActiveOrdersForCookWithYourFilter() {
        User cook = new User();
        cook.setId(1);
        cook.setRole(Role.COOK);
        Mockito.when(userDetailsService.getLoggedUser()).thenReturn(cook);

        Iterable<Order> expectedOrders = Arrays.asList(new Order(), new Order());
        Mockito.when(orderRepository.getActiveOrdersForCertainCook(1)).thenReturn(expectedOrders);

        Iterable<Order> actualOrders = orderService.getActiveOrders("your");

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void testGetActiveOrdersForCookWithFreeFilter() {
        User cook = new User();
        cook.setRole(Role.COOK);
        Mockito.when(userDetailsService.getLoggedUser()).thenReturn(cook);

        Iterable<Order> expectedOrders = Arrays.asList(new Order(), new Order());
        Mockito.when(orderRepository.getActiveOrderWithoutCook()).thenReturn(expectedOrders);

        Iterable<Order> actualOrders = orderService.getActiveOrders("free");

        assertEquals(expectedOrders, actualOrders);
    }


    @Test
    public void testGetActiveOrdersWithInvalidFilter() {
        User user = new User();
        Mockito.when(userDetailsService.getLoggedUser()).thenReturn(user);

        Iterable<Order> actualOrders = orderService.getActiveOrders("invalid_filter");

        assertEquals(new ArrayList<Order>(), actualOrders);
    }
    @Test
    public void testChangeStatusInvalidStatus() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.SERVED);
        User waiter = new User();
        waiter.setId(1);
        order.setWaiter(waiter);
        when(userDetailsService.getLoggedUser()).thenReturn(waiter);

        orderService.ChangeStatus(order);

        verify(orderRepository, times(1)).save(order);
        assertEquals(OrderStatus.SERVED, order.getOrderStatus());
    }
    @Test
    public void testChangeStatus() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setRole(Role.WAITER);
        userRepository.save(user);
        when(userDetailsService.getLoggedUser()).thenReturn(user);

        RestaurantTable table = new RestaurantTable();
        table.setNumber(1);
        restaurantTablesRepository.save(table);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.TAKING);
        order.setId(1);
        order.setTable(table);
        order.setWaiter(user);
        orderRepository.save(order);

        orderService.ChangeStatus(order);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        Order updatedOrder = orderRepository.findById(order.getId()).orElse(null);
        assertEquals(OrderStatus.TAKEN, updatedOrder.getOrderStatus());
    }



}
