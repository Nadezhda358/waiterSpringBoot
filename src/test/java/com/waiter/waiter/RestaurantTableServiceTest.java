package com.waiter.waiter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import com.waiter.waiter.services.RestaurantTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class RestaurantTableServiceTest {

    @Mock
    private RestaurantTablesRepository restaurantTablesRepository;
    @Mock
    private OrderRepository orderRepository;

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
    @Test
    public void testGetAllTables() {
        List<RestaurantTable> mockTables = new ArrayList<>();
        mockTables.add(new RestaurantTable());
        mockTables.add(new RestaurantTable());
        mockTables.add(new RestaurantTable());
        when(restaurantTablesRepository.findAll()).thenReturn(mockTables);
        Iterable<RestaurantTable> actualTables = restaurantTableService.getAllTables();

        assertThat(actualTables, containsInAnyOrder(mockTables.toArray())); // assert that the actual result matches the expected result
    }

    @Test
    public void testGetAllTablesWithNoTables() {
        when(restaurantTablesRepository.findAll()).thenReturn(Collections.emptyList());
        Iterable<RestaurantTable> actualTables = restaurantTableService.getAllTables();
        assertThat(actualTables, is(emptyIterable()));
    }
    @Test
    public void testGetTableByIdWithExistingTable() {
        RestaurantTable mockTable = new RestaurantTable();
        when(restaurantTablesRepository.findById(1)).thenReturn(Optional.of(mockTable));
        RestaurantTable actualTable = restaurantTableService.getTableById(1);
        assertThat(actualTable, is(mockTable));
    }

    //@Test
    //void testGetTableByIdWithNonExistingTable() {
    //    when(restaurantTablesRepository.findById(2)).thenReturn(Optional.empty()); // mock the behavior of the RestaurantTablesRepository
//
    //    RestaurantTable actualTable = restaurantTableService.getTableById(2); // call the method to be tested
//
    //    assertNotNull(actualTable); // assert that the actual result is not null
    //    assertEquals(new RestaurantTable(), actualTable); // assert that the actual result is an empty RestaurantTable object
    //    assertEquals(0, actualTable.getId()); // assert that the ID of the actual result is 0
    //    assertNull(actualTable.getNumber()); // assert that the number of the actual result is null
    //}
    @Test
    public void testGetTableById_tableExists() {
        RestaurantTable table = new RestaurantTable();
        table.setId(1);

        Mockito.when(restaurantTablesRepository.findById(1)).thenReturn(Optional.of(table));

        RestaurantTable result = restaurantTableService.getTableById(1);

        assertEquals(table, result);
    }

    @Test
    public void testGetTableById_tableDoesNotExist() {
        Mockito.when(restaurantTablesRepository.findById(1)).thenReturn(Optional.empty());

        RestaurantTable result = restaurantTableService.getTableById(1);

        assertNotNull(result);
        assertNotEquals(new RestaurantTable(), result);
    }
    @Test
    public void testGetTableIdByOrderId_orderExists() {
        Order order = new Order();
        order.setId(1);

        RestaurantTable table = new RestaurantTable();
        table.setId(2);

        order.setTable(table);

        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        int result = restaurantTableService.getTableIdByOrderId(1);

        assertEquals(2, result);
    }

    //@Test
    //public void testGetTableIdByOrderId_orderDoesNotExist() {
    //    Mockito.when(orderRepository.findById(1)).thenReturn(Optional.empty());
    //    int result = restaurantTableService.getTableIdByOrderId(1);
    //    assertEquals(-1, result);
    //}
}
