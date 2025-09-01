package com.svecw.tastego.controller;

import com.svecw.tastego.model.MenuItem;
import com.svecw.tastego.model.Order;
import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.model.RestaurantMenu;
import com.svecw.tastego.repository.RestaurantMenuRepository;
import com.svecw.tastego.repository.OrderRepository;
import com.svecw.tastego.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    // View today's assigned restaurant(s)
    @GetMapping("/restaurant")
public ResponseEntity<List<Restaurant>> getTodayRestaurants() {
    LocalDate today = LocalDate.now();  // Correct type
    List<Restaurant> restaurants = restaurantRepo.findByDate(today); // Define 'restaurants'
    return ResponseEntity.ok(restaurants);
}


    // View menu for selected restaurant
    @GetMapping("/menu/{restaurantName}")
    public ResponseEntity<?> getMenu(@PathVariable String restaurantName) {
        RestaurantMenu menu = restaurantMenuRepository.findByRestaurantName(restaurantName);
        if (menu == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu not found for restaurant: " + restaurantName);
        }
        List<MenuItem> menuItems = menu.getMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    // Upload UPI Scanner file
    @PostMapping("/upload-scanner")
    public ResponseEntity<String> uploadScanner(@RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        String uploadDir = "uploads/scanners/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String uniqueName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        file.transferTo(new File(uploadDir + uniqueName));

        return ResponseEntity.ok(uniqueName);
    }

    // Place order - use IDs and quantity, with scanner file name for payment screenshot
    @PostMapping("/place-order")
    public ResponseEntity<String> placeOrder(
            @RequestParam String studentId,
            @RequestParam String restaurantId,
            @RequestParam String menuItemId,
            @RequestParam int quantity,
            @RequestParam String scannerFileName) {

        Order order = new Order();
        order.setStudentId(studentId);
        order.setRestaurantId(restaurantId);
        order.setMenuItemId(menuItemId);
        order.setQuantity(quantity);
        order.setPaymentScreenshot(scannerFileName);
        order.setStatus("PENDING");
        order.setOrderTime(LocalDateTime.now());
        order.setAcknowledged(false);

        orderRepo.save(order);

        return ResponseEntity.ok("Order placed. Awaiting restaurant verification.");
    }

    // Check token - search by studentId and restaurantId
    @GetMapping("/token")
    public ResponseEntity<String> getToken(@RequestParam String studentId, @RequestParam String restaurantId) {
        Order order = orderRepo.findByStudentIdAndRestaurantId(studentId, restaurantId);
        if (order == null) return ResponseEntity.ok("Order not found.");
        if (!"ACCEPTED".equalsIgnoreCase(order.getStatus())) return ResponseEntity.ok("Order is not yet verified.");
        return ResponseEntity.ok("Your token: " + order.getTokenNumber());
    }

    // Acknowledge food received
    @PutMapping("/acknowledge")
    public ResponseEntity<String> acknowledge(@RequestParam String studentId, @RequestParam String restaurantId) {
        Order order = orderRepo.findByStudentIdAndRestaurantId(studentId, restaurantId);
        if (order == null) return ResponseEntity.notFound().build();

        order.setAcknowledged(true);
        orderRepo.save(order);
        return ResponseEntity.ok("Order acknowledged. Thank you!");
    }

    // Get current order status
    @GetMapping("/order/status")
    public ResponseEntity<?> getOrderStatus(
            @RequestParam String studentId,
            @RequestParam String restaurantId) {

        Order order = orderRepo.findByStudentIdAndRestaurantId(studentId, restaurantId);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No order found.");
        }

        return ResponseEntity.ok(order);
    }
}
