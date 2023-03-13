package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.RestaurantTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    @Query("SELECT o FROM Order o where o.table=:t AND o.orderStatus <> 'PAID'")
    Order getOrderByTableId(@Param("t") Optional<RestaurantTable> t);
    @Query("SELECT o.table FROM Order o WHERE o.waiter.id=:waiterId AND o.orderStatus <> 'PAID'")
    Iterable<RestaurantTable> getWaiterTables(@Param("waiterId") Integer id);
    @Query("SELECT o FROM Order o WHERE o.orderStatus <> 'PAID'")
    Iterable<Order> getAllActiveOrdersForWaiter();
    @Query("SELECT o FROM Order o WHERE o.orderStatus <> 'PAID' AND o.waiter.id=:waiterId")
    Iterable<Order> getActiveOrdersForCertainWaiter(@Param("waiterId") Integer id);
    @Query("SELECT o FROM Order o WHERE o.waiter.id=:waiterId")
    Iterable<Order> getAllOrdersForCertainWaiter(@Param("waiterId") Integer id);
    @Query("SELECT o FROM Order o WHERE o.orderStatus = 'TAKEN' OR o.orderStatus = 'COOKING'") //OR o.cook is null")
    Iterable<Order> getAllActiveOrdersForCook();
    @Query("SELECT o FROM Order o WHERE (o.orderStatus = 'TAKEN' OR o.orderStatus = 'COOKING') AND o.cook.id=:cookId") //OR o.cook is null")
    Iterable<Order> getActiveOrdersForCertainCook(@Param("cookId") Integer id);
    //@Query("SELECT o FROM Order o WHERE o.cook.id=:cookId") //OR o.cook is null")
    //Iterable<Order> getAllOrdersForCertainCook(@Param("cookId") Integer id);
    @Query("SELECT o FROM Order o WHERE o.orderStatus = 'TAKEN' AND o.cook.id is null") //OR o.cook is null")
    Iterable<Order> getActiveOrderWithoutCook();
    @Query("SELECT DATE(o.createdOn) as orderDate, COUNT(*) as orderCount " +
            "FROM Order o GROUP BY DATE(o.createdOn) ORDER BY orderDate DESC")
    Iterable<Object[]> getOrdersCountByDate();
    @Query("SELECT DATE(o.createdOn) as orderDate, COUNT(*) as orderCount " +
            "FROM Order o WHERE o.cook.id=:cookId GROUP BY DATE(o.createdOn) ORDER BY orderDate DESC")
    Iterable<Object[]> getOrdersCountByDateForCertainCook(@Param("cookId") Integer id);
    //@Query("SELECT o FROM Order o WHERE o.createdOn LIKE ':date%'")
    //Iterable<Order> getAllOrdersForCertainDate(@Param("date") String date);
    //@Query("SELECT o FROM Order o WHERE o.createdOn LIKE ':date%' AND o.cook.id=:cookId")
    //Iterable<Order> getCookOrdersForCertainDate(@Param("date") String date, @Param("cookId") Integer cookId);
    @Query("SELECT o FROM Order o WHERE o.createdOn BETWEEN CONCAT(:date, ' 00:00:00') AND CONCAT(:date, ' 23:59:59')")
    Iterable<Order> getAllOrdersForCertainDate(@Param("date") String date);
    @Query("SELECT o FROM Order o WHERE o.createdOn BETWEEN CONCAT(:date, ' 00:00:00') AND CONCAT(:date, ' 23:59:59') AND o.cook.id = :cookId")
    Iterable<Order> getCookOrdersForCertainDate(@Param("date") String date, @Param("cookId") Integer cookId);
    @Query("SELECT o.updatedOn FROM Order o where o.table=:t AND o.orderStatus <> 'PAID'")
    LocalDateTime getUpdatedOnByTable(@Param("t") Optional<RestaurantTable> t);
}
