package com.waiter.waiter;

import com.waiter.waiter.repositories.OrderDishRepository;
import com.waiter.waiter.repositories.OrderDrinkRepository;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.services.OrderDrinkService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderDrinkServiceTest {
    @BeforeEach
    public void setUp(){
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
}
