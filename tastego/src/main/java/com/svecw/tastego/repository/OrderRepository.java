package com.svecw.tastego.repository;

import com.svecw.tastego.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    // Find specific order by student and restaurant
    Order findByStudentIdAndRestaurantId(String studentId, String restaurantId);

    // Get all orders from a restaurant with a specific status
    List<Order> findByRestaurantIdAndStatus(String restaurantId, String status);

    // Find orders by date
    List<Order> findByDate(LocalDate date);

    // Find orders by restaurantId and date
    List<Order> findByRestaurantIdAndDate(String restaurantId, LocalDate date);

    // Other query methods
    List<Order> findByTokenNumber(String tokenNumber);

    List<Order> findByStatus(String status);
}
