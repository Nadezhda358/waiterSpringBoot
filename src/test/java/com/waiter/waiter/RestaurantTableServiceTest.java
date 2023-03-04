package com.waiter.waiter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import com.waiter.waiter.services.RestaurantTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class RestaurantTableServiceTest {

    @Mock
    private RestaurantTablesRepository restaurantTablesRepository;

    @InjectMocks
    private RestaurantTableService restaurantTableService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewTableWithNoExistingTables() {
        when(restaurantTablesRepository.count()).thenReturn(0L);

        restaurantTableService.createNewTable();

        verify(restaurantTablesRepository, times(1)).save(any(RestaurantTable.class));
        ArgumentCaptor<RestaurantTable> argument = ArgumentCaptor.forClass(RestaurantTable.class);
        verify(restaurantTablesRepository).save(argument.capture());
        assertEquals(1, argument.getValue().getNumber());
    }

    @Test
    public void testCreateNewTableWithExistingTables() {
        when(restaurantTablesRepository.count()).thenReturn(3L);
        when(restaurantTablesRepository.getLastTableNumber()).thenReturn(3);

        restaurantTableService.createNewTable();

        verify(restaurantTablesRepository).save(argThat(table -> table.getNumber() == 4));
    }

    @Test
    public void testCreateNewTableWithSaveFailure() {
        when(restaurantTablesRepository.count()).thenReturn(0L);
        when(restaurantTablesRepository.save(any(RestaurantTable.class))).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> restaurantTableService.createNewTable());
    }
}
