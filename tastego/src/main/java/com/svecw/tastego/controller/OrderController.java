package com.svecw.tastego.controller;

import com.svecw.tastego.model.Order;
import com.svecw.tastego.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepo;

    // Student places an order
    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        if (order.getStudentId() == null || order.getRestaurantId() == null || order.getPaymentScreenshot() == null) {
            return ResponseEntity.badRequest().body("Missing required fields.");
        }

        order.setOrderTime(LocalDateTime.now());
        order.setDate(LocalDate.now());  // Set order date as LocalDate
        order.setStatus("PENDING");
        order.setAcknowledged(false);
        orderRepo.save(order);

        return ResponseEntity.ok("Order placed successfully. Awaiting verification.");
    }

    // Restaurant verifies the order
    @PutMapping("/verify")
    public ResponseEntity<String> verifyOrder(@RequestParam String studentId, @RequestParam String restaurantId) {
        Order order = orderRepo.findByStudentIdAndRestaurantId(studentId, restaurantId);
        if (order == null) return ResponseEntity.notFound().build();

        String token = String.valueOf((int) (Math.random() * 9000) + 1000);  // generate 4-digit token
        order.setStatus("VERIFIED");
        order.setTokenNumber(token);
        orderRepo.save(order);

        return ResponseEntity.ok("Order verified ✅. Token: " + token);
    }

    // Student checks token status
    @GetMapping("/token")
    public ResponseEntity<String> getToken(@RequestParam String studentId, @RequestParam String restaurantId) {
        Order order = orderRepo.findByStudentIdAndRestaurantId(studentId, restaurantId);
        if (order == null) return ResponseEntity.ok("Order not found.");
        if (!"VERIFIED".equals(order.getStatus())) return ResponseEntity.ok("Order is not yet verified.");
        return ResponseEntity.ok("Your token: " + order.getTokenNumber());
    }

    // Student acknowledges the order
    @PutMapping("/acknowledge")
    public ResponseEntity<String> acknowledgeOrder(@RequestParam String studentId, @RequestParam String restaurantId) {
        Order order = orderRepo.findByStudentIdAndRestaurantId(studentId, restaurantId);
        if (order == null) return ResponseEntity.notFound().build();

        order.setAcknowledged(true);
        orderRepo.save(order);
        return ResponseEntity.ok("Order acknowledged by student.");
    }

    // Get all pending orders for a restaurant
    @GetMapping("/pending/{restaurantId}")
    public List<Order> getPending(@PathVariable String restaurantId) {
        return orderRepo.findByRestaurantIdAndStatus(restaurantId, "PENDING");
    }

    // Admin: Get all orders placed today
    @GetMapping("/all/today")
    public ResponseEntity<List<Order>> getTodayOrders() {
        LocalDate today = LocalDate.now();
        List<Order> orders = orderRepo.findByDate(today);  // Use LocalDate, not String
        return ResponseEntity.ok(orders);
    }
}
