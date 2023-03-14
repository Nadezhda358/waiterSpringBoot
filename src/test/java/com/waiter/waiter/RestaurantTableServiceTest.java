package com.waiter.waiter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.waiter.waiter.controllers.RestaurantTableController;
import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.RestaurantTable;
import com.waiter.waiter.entities.User;
import com.waiter.waiter.repositories.OrderRepository;
import com.waiter.waiter.repositories.RestaurantTablesRepository;
import com.waiter.waiter.services.RestaurantTableService;
import com.waiter.waiter.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


@ExtendWith(MockitoExtension.class)
public class RestaurantTableServiceTest {

    @Mock
    private RestaurantTablesRepository restaurantTablesRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    UserDetailsServiceImpl userDetailsService;

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

    @Test
    public void testGetTableIdByOrderId_orderDoesNotExist() {
        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.empty());
        int result = restaurantTableService.getTableIdByOrderId(1);
        assertEquals(-1, result);
    }

    @Test
    public void testSetCardColorsForTables_NoTables() {
        // Setup
        List<RestaurantTable> tables = new ArrayList<>();
        when(restaurantTablesRepository.findAll()).thenReturn(tables);

        // Execute
        restaurantTableService.setCardColorsForTables();

        // Verify
        verify(restaurantTablesRepository, times(1)).saveAll(tables);
        assertEquals(0, tables.size());
    }
    //@Test
    //public void testSetCardColorsForTables_OrderUpdatedWithin30Minutes() {
    //    // Setup
    //    LocalDateTime updatedOn = LocalDateTime.now().minusMinutes(15);
    //    RestaurantTable table = new RestaurantTable();
    //    List<RestaurantTable> tables = new ArrayList<>();
    //    tables.add(table);
    //    when(restaurantTablesRepository.findAll()).thenReturn(tables);
    //    when(orderRepository.getUpdatedOnByTable(Optional.of(table))).thenReturn(updatedOn);
    //    restaurantTableService.setCardColorsForTables();
    //    verify(restaurantTablesRepository, times(1)).saveAll(tables);
    //    assertEquals("card-green", table.getTableCardBackground());
    //}
    //@Test
    //public void testSetCardColorsForTables_OrderUpdatedWithin30Minutes() {
    //    // Setup
    //    LocalDateTime updatedOn = LocalDateTime.now().minusMinutes(15);
    //    RestaurantTable table = new RestaurantTable();
    //    List<RestaurantTable> tables = new ArrayList<>();
    //    tables.add(table);
    //    when(restaurantTablesRepository.findAll()).thenReturn(tables);
    //    when(orderRepository.getUpdatedOnByTable(Optional.of(table))).thenReturn(updatedOn);
//
    //    // Execute
    //    restaurantTableService.setCardColorsForTables();
//
    //    // Verify
    //    verify(restaurantTablesRepository, times(1)).saveAll(tables);
    //    assertEquals("card-green", table.getTableCardBackground());
    //}


    //@Test
    //public void testGetTables_filterAll() {
    //    // Arrange
    //    String filter = "all";
    //    RestaurantTable restaurantTable1 = new RestaurantTable();
    //    restaurantTable1.setId(1);
    //    restaurantTable1.setNumber(1);
    //    RestaurantTable restaurantTable2 = new RestaurantTable();
    //    restaurantTable2.setId(2);
    //    restaurantTable2.setNumber(2);
    //    RestaurantTable restaurantTable3 = new RestaurantTable();
    //    restaurantTable3.setId(3);
    //    restaurantTable3.setNumber(3);
    //    List<RestaurantTable> expectedTables = Arrays.asList(restaurantTable1, restaurantTable2, restaurantTable3);
    //    when(restaurantTablesRepository.findAll()).thenReturn(expectedTables);
//
    //    // Act
    //    Iterable<RestaurantTable> actualTables = classUnderTest.getTables(filter);
//
    //    // Assert
    //    assertEquals(expectedTables, actualTables);
    //    verify(restaurantTablesRepository).findAll();
    //}

    @Test
    void testGetTablesAll() {
        String filter = "all";
        Iterable<RestaurantTable> expectedTables = Arrays.asList(
                new RestaurantTable(),
                new RestaurantTable(),
                new RestaurantTable(),
                new RestaurantTable()
        );
        when(restaurantTablesRepository.findAll()).thenReturn(expectedTables);

        Iterable<RestaurantTable> actualTables = restaurantTableService.getTables(filter);

        assertEquals(expectedTables, actualTables);
    }

    @Test
    void testGetTablesYour() {
        String filter = "your";
        User loggedUser = new User();
        Iterable<RestaurantTable> expectedTables = Arrays.asList(
                new RestaurantTable(),
                new RestaurantTable()
        );
        when(userDetailsService.getLoggedUser()).thenReturn(loggedUser);
        when(orderRepository.getWaiterTables(loggedUser.getId())).thenReturn(expectedTables);

        Iterable<RestaurantTable> actualTables = restaurantTableService.getTables(filter);

        assertEquals(expectedTables, actualTables);
    }
    @Test
    void testGetTablesNotTaken() {
        String filter = "notTaken";
        Iterable<RestaurantTable> expectedTables = Arrays.asList(
                new RestaurantTable(),
                new RestaurantTable()
        );
        when(restaurantTablesRepository.getFreeTables()).thenReturn(expectedTables);

        Iterable<RestaurantTable> actualTables = restaurantTableService.getTables(filter);

        assertEquals(expectedTables, actualTables);
    }

    @Test
    void testGetTablesDefault() {
        String filter = "invalidFilter";
        Iterable<RestaurantTable> expectedTables = new ArrayList<>();
        Iterable<RestaurantTable> actualTables = restaurantTableService.getTables(filter);
        assertEquals(expectedTables, actualTables);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void testSetCardColorsForTables_NoOrders() {
        // Setup
        List<RestaurantTable> tables = new ArrayList<>();
        RestaurantTable table1 = new RestaurantTable();
        table1.setId(1);
        table1.setHasOrder(false);
        tables.add(table1);
        RestaurantTable table2 = new RestaurantTable();
        table2.setId(2);
        table2.setHasOrder(false);
        tables.add(table2);
        when(restaurantTablesRepository.findAll()).thenReturn(tables);

        // Execute
        restaurantTableService.setCardColorsForTables();

        // Verify
        verify(restaurantTablesRepository, times(1)).saveAll(tables);
        assertEquals(2, tables.size());
        assertEquals("card-white", table1.getTableCardBackground());
        assertEquals("card-white", table2.getTableCardBackground());
    }

    @Test
    public void testSetCardColorsForTables_OneOrderLessThan30Minutes() {
        // Setup
        List<RestaurantTable> tables = new ArrayList<>();
        RestaurantTable table1 = new RestaurantTable();
        table1.setId(1);
        table1.setHasOrder(true);
        tables.add(table1);
        LocalDateTime updatedOn = LocalDateTime.now().minus(Duration.ofMinutes(20));
        when(orderRepository.getUpdatedOnByTable(Optional.of(table1))).thenReturn(updatedOn);
        when(restaurantTablesRepository.findAll()).thenReturn(tables);

        // Execute
        restaurantTableService.setCardColorsForTables();

        // Verify
        verify(restaurantTablesRepository, times(1)).saveAll(tables);
        assertEquals(1, tables.size());
        assertEquals("card-green", table1.getTableCardBackground());
    }

    @Test
    public void testSetCardColorsForTables_OneOrderMoreThan30Minutes() {
        // Setup
        List<RestaurantTable> tables = new ArrayList<>();
        RestaurantTable table1 = new RestaurantTable();
        table1.setId(1);
        table1.setHasOrder(true);
        tables.add(table1);
        LocalDateTime updatedOn = LocalDateTime.now().minus(Duration.ofMinutes(40));
        when(orderRepository.getUpdatedOnByTable(Optional.of(table1))).thenReturn(updatedOn);
        when(restaurantTablesRepository.findAll()).thenReturn(tables);

        // Execute
        restaurantTableService.setCardColorsForTables();

        // Verify
        verify(restaurantTablesRepository, times(1)).saveAll(tables);
        assertEquals(1, tables.size());
        assertEquals("card-red", table1.getTableCardBackground());
    }

    @Test
    public void testSetCardColorsForTables_OneOrderExactly30Minutes() {
        // Setup
        List<RestaurantTable> tables = new ArrayList<>();
        RestaurantTable table1 = new RestaurantTable();
        table1.setId(1);
        table1.setHasOrder(true);
        tables.add(table1);
        LocalDateTime updatedOn = LocalDateTime.now().minus(Duration.ofMinutes(30));
        when(orderRepository.getUpdatedOnByTable(Optional.of(table1))).thenReturn(updatedOn);
        when(restaurantTablesRepository.findAll()).thenReturn(tables);

        // Execute
        restaurantTableService.setCardColorsForTables();

        // Verify
        verify(restaurantTablesRepository, times(1)).saveAll(tables);
        assertEquals(1, tables.size());
        assertEquals("card-red", table1.getTableCardBackground());
    }


}
