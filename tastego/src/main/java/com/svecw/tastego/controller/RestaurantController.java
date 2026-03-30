package com.svecw.tastego.controller;

import com.svecw.tastego.model.Order;
import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.repository.OrderRepository;
import com.svecw.tastego.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;

    // 1. Login with Code
    @PostMapping("/login")
    public ResponseEntity<?> loginByCode(@RequestParam String code) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByCode(code);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            // Assuming date in Restaurant model is LocalDate
            if (restaurant.getDate() != null && restaurant.getDate().equals(LocalDate.now())) {
                return ResponseEntity.ok(restaurant);
            }
        }
        return ResponseEntity.status(401).body("Invalid or expired code");
    }

    // 2. View All Student Orders for the restaurant today
    @GetMapping("/orders/{restaurantName}")
    public ResponseEntity<?> viewOrders(@PathVariable String restaurantName) {
        LocalDate today = LocalDate.now();
        List<Order> orders = orderRepository.findByRestaurantNameAndDate(restaurantName, today);
        return ResponseEntity.ok(orders);
    }

    // 3. Accept Order After Payment Verification
    @PostMapping("/accept")
    public ResponseEntity<?> acceptOrder(@RequestParam String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus("ACCEPTED");
            String token = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            order.setTokenNumber(token);
            orderRepository.save(order);
            return ResponseEntity.ok("Order accepted with token: " + token);
        }
        return ResponseEntity.status(404).body("Order not found");
    }
@GetMapping("/order-status")
public ResponseEntity<?> getOrderStatus() {

    LocalDate today =
            LocalDate.now();

    List<Restaurant> restaurants =
            restaurantRepository
            .findByDate(today);

    if (restaurants.isEmpty()) {

        return ResponseEntity.ok(
                "CLOSED"
        );

    }

    Restaurant restaurant =
            restaurants.get(0);

    LocalTime now =
            LocalTime.now();

    LocalTime open =
            LocalTime.parse(
                    restaurant.getOpenTime()
            );

    LocalTime close =
            LocalTime.parse(
                    restaurant.getCloseTime()
            );

    if (now.isAfter(open)
            &&
        now.isBefore(close)) {

        return ResponseEntity.ok(
                "OPEN"
        );

    }

    return ResponseEntity.ok(
            "CLOSED"
    );

}
@GetMapping("/today-timing")
public ResponseEntity<?> getTodayTiming() {

    LocalDate today =
            LocalDate.now();

    List<Restaurant> restaurants =
            restaurantRepository
            .findByDate(today);

    if (restaurants.isEmpty()) {

        return ResponseEntity.ok(
                Map.of(
                        "status", "NO_RESTAURANT"
                )
        );

    }

    Restaurant restaurant =
            restaurants.get(0);

    return ResponseEntity.ok(
            Map.of(
                    "restaurantName",
                    restaurant.getName(),

                    "openTime",
                    restaurant.getOpenTime(),

                    "closeTime",
                    restaurant.getCloseTime()
            )
    );

}
    // 4. Reject Order
    @PostMapping("/reject")
    public ResponseEntity<?> rejectOrder(@RequestParam String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus("REJECTED");
            orderRepository.save(order);
            return ResponseEntity.ok("Order rejected");
        }
        return ResponseEntity.status(404).body("Order not found");
    }
}
