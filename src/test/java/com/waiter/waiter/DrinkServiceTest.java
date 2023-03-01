package com.waiter.waiter;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Drink;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.repositories.DrinkRepository;
import com.waiter.waiter.services.DishService;
import com.waiter.waiter.services.DrinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DrinkServiceTest {
    @InjectMocks
    private DrinkService drinkService;

    @Mock
    private DrinkRepository drinkRepository;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveDrinkWithNoErrors() {
        Drink drink = new Drink();
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = drinkService.saveDrink(drink, bindingResult);
        assertEquals("index", result);
    }

    @Test
    public void testSaveDrinkWithErrors() {
        Drink drink = new Drink();
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = drinkService.saveDrink(drink, bindingResult);
        assertEquals("/menu/add-drink", result);
    }

    @Test
    public void testSaveDrinkSavesDish() {
        Drink drink = new Drink();
        when(bindingResult.hasErrors()).thenReturn(false);
        drinkService.saveDrink(drink, bindingResult);
        verify(drinkRepository, times(1)).save(drink);
    }
}
