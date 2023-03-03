package com.waiter.waiter;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.services.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DishServiceTest {
    @InjectMocks
    private DishService dishService;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveDishWithNoErrors() {
        Dish dish = new Dish();
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = dishService.saveDish(dish, bindingResult, "");
        assertEquals("/menu/menu", result);
    }

    @Test
    public void testSaveDishWithErrors() {
        Dish dish = new Dish();
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = dishService.saveDish(dish, bindingResult, "");
        assertEquals("", result);
    }

    @Test
    public void testSaveDishSavesDish() {
        Dish dish = new Dish();
        when(bindingResult.hasErrors()).thenReturn(false);
        dishService.saveDish(dish, bindingResult, "");
        verify(dishRepository, times(1)).save(dish);
    }
}
