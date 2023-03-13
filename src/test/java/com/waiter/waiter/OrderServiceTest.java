package com.waiter.waiter;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import com.waiter.waiter.entities.OrderDrink;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.enums.OrderStatus;
import com.waiter.waiter.enums.Role;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderService;
import com.waiter.waiter.services.UserDetailsServiceImpl;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;
    @Mock
    private UserDetailsService userDetailsService;

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
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsOrderDrinkNullWithNonEmptyList() {
        List<OrderDrink> orderDrink = new ArrayList<>();
        orderDrink.add(new OrderDrink());
        boolean result = orderService.isOrderDrinkNull(orderDrink);
        Assertions.assertFalse(result);
    }

    @Test
    public void testIsOrderDishNullWithEmptyList() {
        List<OrderDish> orderDish = new ArrayList<>();
        boolean result = orderService.isOrderDishNull(orderDish);
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsOrderDishNullWithNonEmptyList() {
        List<OrderDish> orderDish = new ArrayList<>();
        orderDish.add(new OrderDish());
        boolean result = orderService.isOrderDishNull(orderDish);
        Assertions.assertFalse(result);
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

    //@Test
    //public void testGetOrdersForCertainDateWithYourFilter() {
    //    String date = "2022-03-15";
    //    String filter = "your";
    //    int cookId = 3;
    //    List<Order> expectedOrders = Arrays.asList(new Order(3, "Bob", "Smith"), new Order(4, "Alice", "Smith"));
    //    Mockito.when(userDetailsService.geLoggedUser()).thenReturn(new User(cookId, "Cook", "cook@example.com"));
    //    Mockito.when(orderRepository.getCookOrdersForCertainDate(date, cookId)).thenReturn(expectedOrders);
//
    //    Iterable<Order> actualOrders = orderService.getOrdersForCertainDate(date, filter);
//
    //    Assertions.assertEquals(expectedOrders, actualOrders);
    //}

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
        Iterable<Object[]> expectedOrders = Arrays.asList(new Object[][] {
                {"2023-03-12", 2},
                {"2023-03-11", 1},
                {"2023-03-10", 1}
        });
        when(orderRepository.getOrdersCountByDate()).thenReturn(expectedOrders);

        // Act
        Iterable<Object[]> actualOrders = orderService.getOrdersForCookByDate(filter1);

        // Assert
        assertEquals(expectedOrders, actualOrders);
    }
    @Test
    public void testGetOrdersForCookByDate_all() {
        // Mock data
        List<Object[]> orderData = new ArrayList<>();
        orderData.add(new Object[]{"2023-03-12", 2L});
        orderData.add(new Object[]{"2023-03-11", 1L});
        Mockito.when(orderRepository.getOrdersCountByDate()).thenReturn(orderData);

        // Test method
        Iterable<Object[]> orders = orderService.getOrdersForCookByDate("all");

        // Verify results
        assertEquals(orderData, orders);
    }

    //@Test
    //public void testGetOrdersForCookByDate_your() {
    //    // Mock data
    //    Integer userId = 1;
    //    Mockito.when(userDetailsService.getLoggedUser()).thenReturn(new User(userId));
    //    List<Object[]> orderData = new ArrayList<>();
    //    orderData.add(new Object[]{"2023-03-12", 1L});
    //    Mockito.when(orderRepository.getOrdersCountByDateForCertainCook(userId)).thenReturn(orderData);
//
    //    // Test method
    //    Iterable<Object[]> orders = orderService.getOrdersForCookByDate("your");
//
    //    // Verify results
    //    assertEquals(orderData, orders);
    //}

    @Test
    public void testGetOrdersForCookByDate_invalidFilter() {
        // Test method with invalid filter
        Iterable<Object[]> orders = orderService.getOrdersForCookByDate("invalid");

        // Verify empty result
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
        // Mock data
        List<Order> orderData = Arrays.asList(order1, order2, order3);
        Mockito.when(orderRepository.findAll()).thenReturn(orderData);

        // Test method
        Iterable<Order> orders = orderService.getOrdersForWaiterByDate("all", "newest");

        // Verify results
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

        // Mock data
        List<Order> orderData = Arrays.asList(order3, order2, order1);
        Mockito.when(orderRepository.findAll()).thenReturn(orderData);

        // Test method
        Iterable<Order> orders = orderService.getOrdersForWaiterByDate("all", "oldest");

        // Verify results
        assertEquals(orderData, orders);
    }

    //@Test
    //public void testGetOrdersForWaiterByDate_your_newest() {
    //    // Mock data
    //    Integer userId = 1;
    //    Mockito.when(userDetailsService.getLoggedUser()).thenReturn(new User(userId));
    //    List<Order> orderData = Arrays.asList(
    //            new Order(1, LocalDateTime.parse("2023-03-13T10:00:00"), null, null, new Waiter(userId)),
    //            new Order(2, LocalDateTime.parse("2023-03-12T10:00:00"), null, null, new Waiter(2)),
    //            new Order(3, LocalDateTime.parse("2023-03-11T10:00:00"), null, null, new Waiter(userId))
    //    );
    //    Mockito.when(orderRepository.getAllOrdersForCertainWaiter(userId)).thenReturn(orderData);
//
    //    // Test method
    //    Iterable<Order> orders = orderService.getOrdersForWaiterByDate("your", "newest");
//
    //    // Verify results
    //    assertEquals(Arrays.asList(orderData.get(0), orderData.get(2)), orders);
    //}
    //@Test
    //public void testGetActiveOrdersForWaiter_all() {
    //    // Mock user details service
    //    UserDetailsServiceImpl userDetailsService = Mockito.mock(UserDetailsServiceImpl.class);
    //    Mockito.when(userDetailsService.getLoggedUser()).thenReturn(new User(1, "waiter", Role.WAITER));
//
    //    // Mock order repository
    //    Order order1 = new Order();
    //    order1.setId(1);
    //    order1.setOrderStatus(OrderStatus.TAKEN);
    //    Order order2 = new Order();
    //    order2.setId(2);
    //    order2.setOrderStatus(OrderStatus.COOKING);
    //    Order order3 = new Order();
    //    order3.setId(3);
    //    order3.setOrderStatus(OrderStatus.PAID);
    //    List<Order> orderData = Arrays.asList(order1, order2, order3);
    //    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    //    Mockito.when(orderRepository.getAllActiveOrdersForWaiter()).thenReturn(orderData);
//
    //    // Test method
    //    OrderService orderService = new OrderService();
    //    Iterable<Order> orders = orderService.getActiveOrders("all");
//
    //    // Verify results
    //    assertEquals(orderData, orders);
    //}
//
    //@Test
    //public void testGetActiveOrdersForWaiter_your() {
    //    // Mock user details service
    //    UserDetailsServiceImpl userDetailsService = Mockito.mock(UserDetailsServiceImpl.class);
    //    Mockito.when(userDetailsService.getLoggedUser()).thenReturn(new User(1, "waiter", Role.WAITER));
//
    //    // Mock order repository
    //    Order order1 = new Order();
    //    order1.setId(1);
    //    order1.setOrderStatus(OrderStatus.TAKEN);
    //    Order order2 = new Order();
    //    order2.setId(2);
    //    order2.setOrderStatus(OrderStatus.COOKING);
    //    Order order3 = new Order();
    //    order3.setId(3);
    //    order3.setOrderStatus(OrderStatus.PAID);
    //    List<Order> orderData = Arrays.asList(order1, order2, order3);
    //    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    //    Mockito.when(orderRepository.getActiveOrdersForCertainWaiter(Mockito.anyInt())).thenReturn(orderData);
//
    //    // Test method
    //    OrderService orderService = new OrderService();
    //    Iterable<Order> orders = orderService.getActiveOrders("your");
//
    //    // Verify results
    //    assertEquals(orderData, orders);
    //}

}
