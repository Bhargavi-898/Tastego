package com.svecw.tastego.controller;

import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.model.Order;
import com.svecw.tastego.repository.RestaurantRepository;
import com.svecw.tastego.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import com.svecw.tastego.model.OrderTiming;
import com.svecw.tastego.repository.OrderTimingRepository;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private RestaurantRepository restaurantRepository;
@Autowired
private OrderTimingRepository timingRepository;
    @Autowired
    private OrderRepository orderRepository;
    

    @PostMapping("/add-restaurant")
    public ResponseEntity<?> addRestaurant(
            @RequestBody Map<String, Object> request) {

        String name = (String) request.get("name");
        String dateStr = (String) request.get("date");
        String adminEmail = (String) request.get("adminEmail");
        String providedCode = (String) request.get("code");

        // OPTIONAL timing fields
        String openTime = (String) request.get("openTime");
        String closeTime = (String) request.get("closeTime");

        // ✅ FIX: Only required fields
        if (name == null || dateStr == null || adminEmail == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Missing details"));
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid date format"));
        }

        // Generate or use provided code
        String finalCode;
        if (providedCode != null && !providedCode.trim().isEmpty()) {
            finalCode = providedCode;
        } else {
            finalCode = generateUniqueCode();
        }

        // Create restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setDate(date);
        restaurant.setAdminEmail(adminEmail);
        restaurant.setCode(finalCode);

        // ✅ FIX: timing optional
        if (openTime != null && closeTime != null) {
            restaurant.setOpenTime(openTime);
            restaurant.setCloseTime(closeTime);
        }

        restaurantRepository.save(restaurant);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Restaurant added successfully",
                        "code", finalCode
                )
        );
    }

@PostMapping("/update-timing")
public ResponseEntity<?> updateTiming(
        @RequestBody OrderTiming timing) {

    OrderTiming existing;

    if (timingRepository.count() > 0) {

        existing =
                timingRepository
                .findAll()
                .get(0);

        existing.setOpenTime(
                timing.getOpenTime()
        );

        existing.setCloseTime(
                timing.getCloseTime()
        );

    } else {

        existing = timing;

    }

    timingRepository.save(existing);

    return ResponseEntity.ok(
            "Timing updated successfully"
    );
}
    @GetMapping("/orders/accepted")
public ResponseEntity<?> getAcceptedOrders(
        @RequestParam String restaurantName) {

    try {

        LocalDate today =
                LocalDate.now();

        List<Order> orders =
                orderRepository
                        .findByRestaurantNameAndDate(
                                restaurantName,
                                today
                        );

        if (orders == null || orders.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<Order> accepted =
                orders.stream()
                        .filter(o ->
                                "ACCEPTED".equalsIgnoreCase(
                                        o.getStatus()
                                )
                        )
                        .toList();

        return ResponseEntity.ok(accepted);

    }

    catch (Exception e) {

        e.printStackTrace();

        return ResponseEntity.ok(List.of());

    }
}
@GetMapping("/timing")
public ResponseEntity<?> getTiming() {

    if (timingRepository.count() == 0) {

        return ResponseEntity.ok(
                "NOT_SET"
        );

    }

    OrderTiming timing =
            timingRepository
            .findAll()
            .get(0);

    return ResponseEntity.ok(timing);
}
    // Keep this as an on-demand admin endpoint if needed for troubleshooting.
    @GetMapping("/cleanup-expired-orders")
    public ResponseEntity<String> cleanupExpiredOrders(){

        LocalDate today = LocalDate.now();

        orderRepository.deleteByDateBefore(today);
        restaurantRepository.deleteByDateBefore(today);

        System.out.println("Old restaurant orders and restaurants deleted");

        return ResponseEntity.ok("Expired orders and restaurants cleaned");
    }

    @PostMapping("/generate-code")
    public ResponseEntity<?> generateCode(@RequestBody Map<String, String> request) {
        String adminEmail = request.get("email");

        if (adminEmail == null) {
            return ResponseEntity.badRequest().body("Admin email required");
        }

        String generatedCode = generateUniqueCode();

        return ResponseEntity.ok(Map.of(
                "message", "Code generated successfully",
                "code", generatedCode
        ));
    }

    @PostMapping("/restaurant/login")
    public ResponseEntity<?> loginRestaurant(@RequestBody Map<String, String> credentials) {
        String name = credentials.get("name");
        String code = credentials.get("code");

        if (name == null || code == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Name and code required"));
        }

        Optional<Restaurant> restaurant = restaurantRepository.findByNameAndCode(name, code);

        if (restaurant.isPresent()) {
            return ResponseEntity.ok(restaurant.get());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid restaurant name or code"));
    }

    @GetMapping("/restaurant/today")
    public ResponseEntity<?> getTodayRestaurants() {
        LocalDate today = LocalDate.now();
        List<Restaurant> restaurants = restaurantRepository.findByDate(today);

        if (restaurants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No restaurant assigned today");
        }

        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants/upcoming")
    public ResponseEntity<?> getUpcomingRestaurants() {
        LocalDate today = LocalDate.now();
        List<Restaurant> allRestaurants = restaurantRepository.findAll();
        List<Restaurant> upcoming = new ArrayList<>();

        for (Restaurant r : allRestaurants) {
            if (r.getDate() != null && !r.getDate().isBefore(today)) {
                upcoming.add(r);
            }
        }

        return ResponseEntity.ok(upcoming);
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
            return ResponseEntity.badRequest()
                    .body("Invalid date format. Use YYYY-MM-DD");
        }
    }

    @DeleteMapping("/clear-expired")
    public ResponseEntity<?> clearExpiredAssignments() {
        LocalDate today = LocalDate.now();
        List<Restaurant> allRestaurants = restaurantRepository.findAll();

        for (Restaurant rest : allRestaurants) {
            if (rest.getDate() != null && rest.getDate().isBefore(today)) {
                restaurantRepository.delete(rest);
            }
        }

        return ResponseEntity.ok("Expired restaurants removed");
    }

  @GetMapping("/orders/accepted-all")
public ResponseEntity<?> getAllAcceptedOrders() {

    LocalDate today =
            LocalDate.now();

    List<Order> orders =
            orderRepository
            .findByStatusAndDate(
                    "ACCEPTED",
                    today
            );

    return ResponseEntity.ok(
            orders
    );

}
@Scheduled(cron = "0 0 0 * * ?")
public void autoDeletePreviousDayData() {

    try {

        LocalDate today =
                LocalDate.now();

        // Delete yesterday's orders

        orderRepository
                .deleteByDateBefore(
                        today
                );

        // Delete yesterday's restaurants

        restaurantRepository
                .deleteByDateBefore(
                        today
                );

        System.out.println(
        "Old restaurant and order data deleted automatically"
        );

    }

    catch (Exception e) {

        e.printStackTrace();

    }

}
    @PutMapping("/order/accept")
    public ResponseEntity<?> acceptOrder(@RequestParam String orderId) {
        try {
            Optional<Order> optional = orderRepository.findById(orderId);

            if (optional.isEmpty()) {
                return ResponseEntity.badRequest().body("Order not found");
            }

            Order order = optional.get();
            int token = new Random().nextInt(900) + 100;

            order.setStatus("ACCEPTED");
            order.setTokenNumber(String.valueOf(token));
            order.setAcknowledged(true);
            order.setOrderTime(LocalDateTime.now());

            orderRepository.save(order);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Order accepted successfully");
            response.put("tokenNumber", String.valueOf(token));
            response.put("orderId", order.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error accepting order");
        }
    }

    @PutMapping("/order/reject")
    public ResponseEntity<?> rejectOrder(@RequestParam String orderId) {
        Optional<Order> optional = orderRepository.findById(orderId);

        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().body("Order not found");
        }

        Order order = optional.get();
        order.setStatus("REJECTED");
        orderRepository.save(order);

        return ResponseEntity.ok("Order rejected");
    }
@GetMapping("/orders/pending")
public ResponseEntity<?> getPendingOrders(
        @RequestParam String restaurantName) {

    try {

        // Get all orders for this restaurant
        List<Order> orders =
                orderRepository
                        .findByRestaurantName(
                                restaurantName
                        );

        if (orders == null || orders.isEmpty()) {

            return ResponseEntity.ok(
                    List.of()
            );

        }

        // Filter only pending orders
        List<Order> pending =
                orders.stream()
                        .filter(o ->
                                "PENDING".equalsIgnoreCase(
                                        o.getStatus()
                                )
                        )
                        .toList();

        return ResponseEntity.ok(
                pending
        );

    }

    catch (Exception e) {

        e.printStackTrace();

        return ResponseEntity.ok(
                List.of()
        );

    }
}
    private String generateUniqueCode() {
        String code;
        do {
            code = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 6)
                    .toUpperCase();
        } while (restaurantRepository.existsByCode(code));

        return code;
    }
}