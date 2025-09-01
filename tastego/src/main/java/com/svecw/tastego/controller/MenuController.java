package com.svecw.tastego.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svecw.tastego.model.MenuItem;
import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.model.RestaurantMenu;
import com.svecw.tastego.repository.RestaurantMenuRepository;
import com.svecw.tastego.repository.RestaurantRepository;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private RestaurantMenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Upload Menu Items for a restaurant
    @PostMapping("/upload")
    public ResponseEntity<?> uploadMenuList(
            @RequestParam("restaurantName") String restaurantName,
            @RequestParam("menuItems") String menuItemsJson,
            @RequestParam(value = "upiImageUrl", required = false) String upiImageUrl
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<MenuItem> menuItems = objectMapper.readValue(menuItemsJson, new TypeReference<List<MenuItem>>() {});

            RestaurantMenu menu = menuRepository.findByRestaurantName(restaurantName);
            if (menu == null) {
                menu = new RestaurantMenu();
                menu.setRestaurantName(restaurantName);
            }

            menu.setMenuItems(menuItems);
            if (upiImageUrl != null && !upiImageUrl.isEmpty()) {
                menu.setUpiImageUrl(upiImageUrl);
            }

            menuRepository.save(menu);
            return ResponseEntity.ok("Menu items uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading menu items: " + e.getMessage());
        }
    }

    // Get Menu by Restaurant Name
    @GetMapping("/{restaurantName}")
    public ResponseEntity<?> getMenuByRestaurant(@PathVariable String restaurantName) {
        try {
            RestaurantMenu menu = menuRepository.findByRestaurantName(restaurantName);
            if (menu == null) {
                return ResponseEntity.status(404).body("No menu found for restaurant: " + restaurantName);
            }
            return ResponseEntity.ok(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving menu: " + e.getMessage());
        }
    }

    // Get Today's Menu (for the restaurant assigned today)
    @GetMapping("/today")
    public ResponseEntity<?> getTodayMenu() {
        try {
            LocalDate today = java.time.LocalDate.now();
List<Restaurant> restaurants = restaurantRepository.findByDate(today);

            if (restaurants.isEmpty()) {
                return ResponseEntity.status(404).body("No restaurant assigned today.");
            }

            String restaurantName = restaurants.get(0).getName();
            RestaurantMenu menu = menuRepository.findByRestaurantName(restaurantName);

            if (menu == null) {
                return ResponseEntity.status(404).body("Menu not found for today's restaurant: " + restaurantName);
            }

            return ResponseEntity.ok(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching today's menu: " + e.getMessage());
        }
    }
}
