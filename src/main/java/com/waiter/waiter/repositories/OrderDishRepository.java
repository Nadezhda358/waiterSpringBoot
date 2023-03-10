package com.waiter.waiter.repositories;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Order;
import com.waiter.waiter.entities.OrderDish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderDishRepository extends CrudRepository<OrderDish, Integer> {
    @Query("SELECT o FROM OrderDish o where o.order=:order")
    List<OrderDish> getOrderInfo(@Param("order") Order order);

    //@Query("Select od.Dish from OrderDish od join Dish d on od.getDish.getId=d.getId where od.getOrder!=:order")
    //@Query("Select o.dish from OrderDish o inner join dishes d on o.dish d where o.order=:order")
    //works bot's version @Query("SELECT d FROM Dish d WHERE d.id NOT IN (SELECT od.dish.id FROM OrderDish od WHERE od.order = :order)")
    //bot's version not working here @Query("SELECT d FROM Dish d LEFT JOIN d.orders o WHERE o.id != :orderId OR o.id IS NULL")
    //bot's version not working again @Query("SELECT d FROM Dish d LEFT JOIN d.orderDishes od WITH od.order = :order WHERE od.id IS NULL")
    //not working again bot @Query("SELECT d FROM Dish d LEFT JOIN OrderDish od ON d.id = od.dish.id AND od.order.id = :orderId WHERE od.id IS NULL")
    //not working bot you better go to sleep @Query("SELECT d FROM Dish d LEFT JOIN (SELECT od.dish.id FROM OrderDish od WHERE od.order.id = :orderId) o ON d.id = o.id WHERE o.id IS NULL")
    @Query("SELECT d FROM Dish d WHERE d.id NOT IN (SELECT od.dish.id FROM OrderDish od WHERE od.order = :order)")
    public List<Dish> getAllNotAddedDishesToOrder(@Param("order") Order order);

    @Query("SELECT sum(od.currentPrice) FROM OrderDish od WHERE od.order = :order")
    public double getTotalCost(@Param("order") Order order);
}
