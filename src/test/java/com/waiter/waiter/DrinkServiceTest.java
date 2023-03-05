package com.waiter.waiter;

import com.waiter.waiter.entities.Drink;
import com.waiter.waiter.repositories.DrinkRepository;
import com.waiter.waiter.services.DrinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.validation.BindingResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void testGetDrinkByIdWithValidId() {
        Integer drinkId = 1;
        Drink drink = new Drink();
        Optional<Drink> optionalDrink = Optional.of(drink);
        Mockito.when(drinkRepository.findById(drinkId)).thenReturn(optionalDrink);
        Drink result = drinkService.getDrinkById(drinkId);
        assertNotNull(result);
        assertEquals(drink, result);
    }

    //@Test
    //public void testGetDrinkByIdWithInvalidId() {
    //    Integer drinkId = 1;
    //    Optional<Drink> optionalDrink = Optional.empty();
    //    Mockito.when(drinkRepository.findById(drinkId)).thenReturn(optionalDrink);
    //    Drink result = drinkService.getDrinkById(drinkId);
    //    assertNotNull(result);
    //    assertEquals(new Drink(), result);
    //}

    @Test
    public void testDeleteDrinkByIdWithValidId() {
        Integer drinkId = 1;
        drinkService.deleteDrinkById(drinkId);
        Mockito.verify(drinkRepository, Mockito.times(1)).deleteById(drinkId);
    }

    @Test
    public void testDeleteDrinkByIdWithInvalidId() {
        Integer drinkId = 99; // invalid ID
        doThrow(EmptyResultDataAccessException.class).when(drinkRepository).deleteById(drinkId);
        assertThrows(EmptyResultDataAccessException.class, () -> drinkService.deleteDrinkById(drinkId));
    }
}
