package com.svecw.tastego.repository;

import com.svecw.tastego.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;
public interface OrderRepository extends MongoRepository<Order, String> {

    // Find specific order by student email and restaurant
List<Order> findByStudentEmailAndRestaurantId(
        String studentEmail,
        String restaurantId);
List<Order> findByStudentEmailAndStatus(
        String studentEmail,
        String status);
    // Get all orders from a restaurant with a specific status
List<Order> findByRestaurantNameAndStatus(
        String restaurantName,
        String status);
    // Find orders by date
    List<Order> findByDate(LocalDate date);

    // Find orders by restaurant name and date
    List<Order> findByRestaurantNameAndDate(String restaurantName, LocalDate date);

    // Find orders by token number
    List<Order> findByTokenNumber(String tokenNumber);
List<Order> findByStudentEmailAndRestaurantName(
        String studentEmail,
        String restaurantName
);
void deleteByRestaurantNameAndDateBefore(
        String restaurantName,
        LocalDate date
);
void deleteByRestaurantNameNot(String restaurantName);
    // Find orders by status
    List<Order> findByStatus(String status);
    List<Order> findByRestaurantName(String restaurantName);
    // Delete previous day orders automatically
    void deleteByDateBefore(LocalDate date);
    List<Order> findByStatusAndDate(
        String status,
        LocalDate date
);
List<Order> findByStudentEmailAndRestaurantNameAndOrderTimeBetween(
        String studentEmail,
        String restaurantName,
        LocalDateTime start,
        LocalDateTime end
);
}