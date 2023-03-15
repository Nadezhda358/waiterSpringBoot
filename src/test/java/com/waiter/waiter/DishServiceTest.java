package com.waiter.waiter;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.repositories.DishRepository;
import com.waiter.waiter.services.DishService;
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
        assertEquals("redirect:/menu", result);
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

    @Test
    public void testGetAllDishes() {
        List<Dish> dishList = new ArrayList<>();
        Dish dish1 = new Dish();
        Dish dish2 = new Dish();
        dishList.add(dish1);
        dishList.add(dish2);
        Mockito.when(dishRepository.findAll()).thenReturn(dishList);
        Iterable<Dish> result = dishService.getAllDishes();
        assertNotNull(result);
        assertEquals(2, ((Collection<?>) result).size());
    }

    @Test
    public void testGetAllDishesWithNoDishes() {
        List<Dish> dishList = new ArrayList<>();
        Mockito.when(dishRepository.findAll()).thenReturn(dishList);
        Iterable<Dish> result = dishService.getAllDishes();
        assertNotNull(result);
        assertEquals(0, ((Collection<?>) result).size());
    }

    @Test
    public void testGetDishByIdWithValidId() {
        Integer dishId = 1;
        Dish dish = new Dish();
        Optional<Dish> optionalDish = Optional.of(dish);
        Mockito.when(dishRepository.findById(dishId)).thenReturn(optionalDish);
        Dish result = dishService.getDishById(dishId);
        assertNotNull(result);
        assertEquals(dish, result);
    }


    @Test
    public void testDeleteDishByIdWithValidId() {
        Integer dishId = 1;
        dishService.deleteDishById(dishId);
        Mockito.verify(dishRepository, Mockito.times(1)).deleteById(dishId);
    }

    @Test
    public void testDeleteDishByIdWithInvalidId() {
        Integer dishId = 99;
        doThrow(EmptyResultDataAccessException.class).when(dishRepository).deleteById(dishId);
        assertThrows(EmptyResultDataAccessException.class, () -> dishService.deleteDishById(dishId));
    }
}
