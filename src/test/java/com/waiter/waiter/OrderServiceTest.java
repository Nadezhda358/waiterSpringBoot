package com.waiter.waiter;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderService;
import net.bytebuddy.matcher.ElementMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;
    @BeforeEach
    public void setUp(){
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
}
