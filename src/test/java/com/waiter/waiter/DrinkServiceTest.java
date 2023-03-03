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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        String result = drinkService.saveDrink(drink, bindingResult, "");
        assertEquals("redirect:/menu", result);
    }

    @Test
    public void testSaveDrinkWithErrors() {
        Drink drink = new Drink();
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = drinkService.saveDrink(drink, bindingResult, "");
        assertEquals("", result);
    }

    @Test
    public void testSaveDrinkSavesDish() {
        Drink drink = new Drink();
        when(bindingResult.hasErrors()).thenReturn(false);
        drinkService.saveDrink(drink, bindingResult, "");
        verify(drinkRepository, times(1)).save(drink);
    }

    @Test
    public void testGetAllDrinks() {
        List<Drink> drinkList = new ArrayList<>();
        Drink drink1 = new Drink();
        Drink drink2 = new Drink();
        drinkList.add(drink1);
        drinkList.add(drink2);
        Mockito.when(drinkRepository.findAll()).thenReturn(drinkList);
        Iterable<Drink> result = drinkService.getAllDrinks();
        assertNotNull(result);
        assertEquals(2, ((Collection<?>) result).size());
    }

    @Test
    public void testGetAllDrinksWithNoDrinks() {
        List<Drink> drinkList = new ArrayList<>();
        Mockito.when(drinkRepository.findAll()).thenReturn(drinkList);
        Iterable<Drink> result = drinkService.getAllDrinks();
        assertNotNull(result);
        assertEquals(0, ((Collection<?>) result).size());
    }
}
