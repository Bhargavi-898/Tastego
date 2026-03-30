package com.svecw.tastego.controller;

import com.svecw.tastego.model.Order;
import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.repository.OrderRepository;
import com.svecw.tastego.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.svecw.tastego.model.OrderTiming;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import com.svecw.tastego.repository.OrderTimingRepository;
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepo;
@Autowired
private OrderTimingRepository timingRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    // ================================
    // student places order
    // ================================

   @PostMapping("/place")
public ResponseEntity<String> placeOrder(
        @RequestBody Order order) {

    try {

        LocalDate today =
                LocalDate.now();

        // ===============================
        // CHECK RESTAURANT AVAILABLE
        // ===============================

        List<Restaurant> restaurants =
                restaurantRepository
                .findByDate(today);

        if (restaurants.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                    "No restaurant available today"
                    );

        }

        // ===============================
        // CHECK GLOBAL TIMING
        // ===============================

        List<OrderTiming> timings =
                timingRepository
                .findAll();

        if (timings.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                    "Order timing not set"
                    );

        }

        OrderTiming timing =
                timings.get(0);

        if (timing.getOpenTime() == null ||
            timing.getCloseTime() == null) {

            return ResponseEntity
                    .badRequest()
                    .body(
                    "Order timing not configured"
                    );

        }

        LocalTime now =
                LocalTime.now();

        LocalTime openTime =
                LocalTime.parse(
                        timing.getOpenTime()
                );

        LocalTime closeTime =
                LocalTime.parse(
                        timing.getCloseTime()
                );

        if (!(now.isAfter(openTime)
                &&
              now.isBefore(closeTime))) {

            return ResponseEntity
                    .badRequest()
                    .body(
                    "Ordering time closed"
                    );

        }

        // ===============================
        // SAVE ORDER
        // ===============================

        order.setOrderTime(
                LocalDateTime.now()
        );

        order.setDate(today);

        order.setStatus("PENDING");

        order.setAcknowledged(false);

        orderRepo.save(order);

        return ResponseEntity.ok(
                "Order placed successfully"
        );

    }

    catch (Exception e) {

        e.printStackTrace();

        return ResponseEntity
                .internalServerError()
                .body(
                "Error placing order"
                );

    }

}
    // ================================
    // admin verifies order
    // ================================

    @PutMapping("/verify/{orderId}")
    public ResponseEntity<String> verifyOrder(
            @PathVariable String orderId) {

        Order order =
                orderRepo
                .findById(orderId)
                .orElse(null);

        if (order == null)

            return ResponseEntity
                    .notFound()
                    .build();

        String token =
                String.valueOf(
                        (int)
                        (Math.random() * 9000)
                        + 1000
                );

        order.setStatus("VERIFIED");

        order.setTokenNumber(token);

        orderRepo.save(order);

        return ResponseEntity.ok(
                "Order verified. Token: "
                + token
        );

    }

    // ================================
    // student get token
    // ================================

    @GetMapping("/token/{orderId}")
    public ResponseEntity<String> getToken(
            @PathVariable String orderId) {

        Order order =
                orderRepo
                .findById(orderId)
                .orElse(null);

        if (order == null)

            return ResponseEntity
                    .notFound()
                    .build();

        return ResponseEntity.ok(
                order.getTokenNumber()
        );

    }

    // ================================
    // admin view today's orders
    // ================================

    @GetMapping("/today")
    public List<Order> getTodayOrders() {

        LocalDate today =
                LocalDate.now();

        return orderRepo
                .findByDate(today);

    }

}