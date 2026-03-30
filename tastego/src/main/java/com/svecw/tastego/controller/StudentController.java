package com.svecw.tastego.controller;

import com.svecw.tastego.model.*;
import com.svecw.tastego.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Arrays;
@RestController
@RequestMapping("/api/student")
@CrossOrigin("*")
public class StudentController {

    @Autowired
    private RestaurantRepository restaurantRepo;

    @Autowired
    private RestaurantMenuRepository restaurantMenuRepository;

    @Autowired
    private OrderRepository orderRepo;

    @GetMapping("/menu/{restaurantName}")
    public ResponseEntity<?> getMenu(@PathVariable String restaurantName) {
        RestaurantMenu menu = restaurantMenuRepository.findByRestaurantName(restaurantName);

        if (menu == null || menu.getMenuItems() == null) {
            return ResponseEntity.badRequest().body("Menu not found");
        }

        return ResponseEntity.ok(menu.getMenuItems());
    }

    @PostMapping("/upload-scanner")
    public ResponseEntity<String> uploadScanner(@RequestParam MultipartFile file) throws IOException {
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" +
                File.separator + "scanners" + File.separator;

        File dir = new File(uploadDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destination = new File(uploadDir + fileName);

        file.transferTo(destination);

        return ResponseEntity.ok(fileName);
    }

    @PostMapping("/place-order")
    public ResponseEntity<String> placeOrder(
            @RequestParam String studentName,
            @RequestParam String studentEmail,
            @RequestParam String restaurantName,
            @RequestParam String menuItemId,
@RequestParam String menuItemNames,
            @RequestParam int quantity,
            @RequestParam String scannerFileName) {

        Order order = new Order();

        order.setStudentName(studentName);
        order.setStudentEmail(studentEmail);
        order.setRestaurantName(restaurantName);
        order.setMenuItemId(menuItemId);
List<String> items =
        Arrays.asList(
                menuItemNames
                        .replace("[","")
                        .replace("]","")
                        .replace("\"","")
                        .split(",")
        );

order.setMenuItemNames(items);        order.setQuantity(quantity);
        order.setPaymentScreenshot(scannerFileName);
        order.setStatus("PENDING");
        order.setTokenNumber(null);
        order.setDate(LocalDate.now());
        order.setOrderTime(LocalDateTime.now());
        order.setAcknowledged(false);

        orderRepo.save(order);

        return ResponseEntity.ok(order.getId());
    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken(
            @RequestParam String studentEmail,
            @RequestParam String restaurantName) {

        List<Order> orders = orderRepo.findByStudentEmailAndRestaurantName(studentEmail, restaurantName);

        if (orders == null || orders.isEmpty()) {
            return ResponseEntity.ok("Order not found");
        }

        Order order = orders.get(orders.size() - 1);

        if ("ACCEPTED".equalsIgnoreCase(order.getStatus())) {
            return ResponseEntity.ok("Order Accepted. Token Number: " + order.getTokenNumber());
        }

        if ("REJECTED".equalsIgnoreCase(order.getStatus())) {
            return ResponseEntity.ok("Order Rejected by Restaurant");
        }

        return ResponseEntity.ok("Waiting for restaurant verification");
    }

    @GetMapping("/today-restaurants")
    public ResponseEntity<?> getTodayRestaurants() {
        List<Restaurant> restaurants = restaurantRepo.findByDate(LocalDate.now());
        return ResponseEntity.ok(restaurants);
    }

@GetMapping("/orders")
public ResponseEntity<?> getStudentOrders(
        @RequestParam String studentEmail) {

    try {

        LocalDate today =
                LocalDate.now();

        // Get today's restaurant

        List<Restaurant> restaurants =
                restaurantRepo.findByDate(today);

        if (restaurants == null ||
            restaurants.isEmpty()) {

            return ResponseEntity.ok(
                    List.of()
            );

        }

        String todayRestaurant =
                restaurants.get(0)
                .getName();

        // Create time range

        LocalDateTime startOfDay =
                today.atStartOfDay();

        LocalDateTime endOfDay =
                today.atTime(23, 59, 59);

        // Fetch only today's orders

        List<Order> orders =
                orderRepo
                .findByStudentEmailAndRestaurantNameAndOrderTimeBetween(
                        studentEmail,
                        todayRestaurant,
                        startOfDay,
                        endOfDay
                );

        return ResponseEntity.ok(
                orders
        );

    }

    catch (Exception e) {

        e.printStackTrace();

        return ResponseEntity.ok(
                List.of()
        );

    }

}

    @GetMapping("/order/status")
    public ResponseEntity<?> getOrderStatus(
            @RequestParam String studentEmail,
            @RequestParam String restaurantId) {

        List<Order> orders = orderRepo.findByStudentEmailAndRestaurantId(studentEmail, restaurantId);

        if (orders == null || orders.isEmpty()) {
            return ResponseEntity.badRequest().body("Order not found");
        }

        Order order = orders.get(orders.size() - 1);

        if ("ACCEPTED".equals(order.getStatus())) {
            return ResponseEntity.ok(
                    java.util.Map.of(
                            "message", "Your order placed",
                            "token", order.getTokenNumber()
                    )
            );
        }

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message", "Waiting for restaurant verification"
                )
        );
    }
}