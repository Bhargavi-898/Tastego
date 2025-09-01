package com.svecw.tastego.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.model.Order;
import com.svecw.tastego.repository.RestaurantRepository;
import com.svecw.tastego.repository.OrderRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Add restaurant with unique code
    @PostMapping("/add-restaurant")
    public ResponseEntity<?> addRestaurant(@RequestBody Restaurant restaurant) {
        if (restaurant.getName() == null || restaurant.getDate() == null || restaurant.getAdminEmail() == null) {
            return ResponseEntity.badRequest().body("Missing details");
        }

        // Generate unique 6-char code
        String generatedCode = generateUniqueCode();
        restaurant.setCode(generatedCode);
        restaurantRepository.save(restaurant);

        return ResponseEntity.ok(Collections.singletonMap("code", generatedCode));
    }

    // Generate unique code for a restaurant by admin email
    @PostMapping("/generate-code")
    public ResponseEntity<?> generateCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }

        List<Restaurant> restaurants = restaurantRepository.findAllByAdminEmail(email);
        if (restaurants.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No restaurants found for this email"));
        }

        Restaurant restaurant = restaurants.get(0); // Take the first one or implement proper selection logic
        String newCode = generateUniqueCode();
        restaurant.setCode(newCode);
        restaurantRepository.save(restaurant);

        return ResponseEntity.ok(Map.of("code", newCode));
    }

    // Restaurant login
    @PostMapping("/restaurant/login")
    public ResponseEntity<?> loginRestaurant(@RequestBody Map<String, String> credentials) {
        String name = credentials.get("name");
        String code = credentials.get("code");

        if (name == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Name and code are required"));
        }

        Restaurant restaurant = restaurantRepository.findByNameAndCode(name, code);
        if (restaurant != null) {
            return ResponseEntity.ok(restaurant);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid restaurant name or code"));
        }
    }

    @GetMapping("/restaurant/today")
    public ResponseEntity<?> getTodayRestaurants() {
        LocalDate today = LocalDate.now();
        List<Restaurant> restaurants = restaurantRepository.findByDate(today);
        if (restaurants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No restaurant assigned today");
        }
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/orders/today")
    public ResponseEntity<?> getTodayOrders() {
        LocalDate today = LocalDate.now();
        List<Order> todayOrders = orderRepository.findByDate(today);
        return ResponseEntity.ok(todayOrders);
    }

    @GetMapping("/orders/view")
    public ResponseEntity<?> viewOrdersByDate(@RequestParam String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            List<Order> orders = orderRepository.findByDate(parsedDate);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use YYYY-MM-DD.");
        }
    }

    @DeleteMapping("/clear-expired")
    public ResponseEntity<?> clearExpiredAssignments() {
        LocalDate today = LocalDate.now();
        List<Restaurant> allRestaurants = restaurantRepository.findAll();

        for (Restaurant rest : allRestaurants) {
            if (rest.getDate() != null && !today.equals(rest.getDate()) && rest.getCode() != null) {
                rest.setCode(null);
                restaurantRepository.save(rest);
            }
        }

        return ResponseEntity.ok("Expired codes cleared.");
    }

    // Utility method to generate a unique 6-character code
    private String generateUniqueCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6).toUpperCase();
        } while (restaurantRepository.existsByCode(code));
        return code;
    }
}
