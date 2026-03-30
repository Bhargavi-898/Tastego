package com.svecw.tastego.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svecw.tastego.model.MenuItem;
import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.model.RestaurantMenu;
import com.svecw.tastego.repository.RestaurantMenuRepository;
import com.svecw.tastego.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private RestaurantMenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    // ================================
    // Upload or Update Menu
    // ================================

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMenuList(
            @RequestParam("restaurantName") String restaurantName,
            @RequestParam("menuItems") String menuItemsJson,
            @RequestParam(value = "upiImageUrl", required = false) String upiImageUrl) {

        try {

            ObjectMapper objectMapper =
                    new ObjectMapper();

            List<MenuItem> menuItems =
                    objectMapper.readValue(
                            menuItemsJson,
                            new TypeReference<List<MenuItem>>() {}
                    );

            // Check if menu already exists

            RestaurantMenu menu =
                    menuRepository
                    .findByRestaurantName(
                            restaurantName
                    );

            if (menu == null) {

                menu =
                        new RestaurantMenu();

                menu.setRestaurantName(
                        restaurantName
                );

            }

            // Update menu items

            menu.setMenuItems(
                    menuItems
            );

            // Update scanner if provided

            if (upiImageUrl != null &&
                !upiImageUrl.isEmpty()) {

                menu.setUpiImageUrl(
                        upiImageUrl
                );

            }

            menuRepository.save(
                    menu
            );

            return ResponseEntity.ok(
                    "Menu saved / updated successfully."
            );

        }

        catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body(
                    "Error uploading menu: "
                    + e.getMessage()
                    );

        }

    }

    // ================================
    // Get Menu by Restaurant
    // ================================

    @GetMapping("/{restaurantName}")
    public ResponseEntity<?> getMenuByRestaurant(
            @PathVariable String restaurantName) {

        try {

            RestaurantMenu menu =
                    menuRepository
                    .findByRestaurantName(
                            restaurantName
                    );

            if (menu == null) {

                return ResponseEntity
                        .status(404)
                        .body(
                        "No menu found for restaurant: "
                        + restaurantName
                        );

            }

            return ResponseEntity.ok(
                    menu
            );

        }

        catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body(
                    "Error retrieving menu: "
                    + e.getMessage()
                    );

        }

    }

    // ================================
    // Get Today's Restaurant Menu
    // ================================

    @GetMapping("/today")
    public ResponseEntity<?> getTodayMenu() {

        try {

            LocalDate today =
                    LocalDate.now();

            List<Restaurant> restaurants =
                    restaurantRepository
                    .findByDate(
                            today
                    );

            if (restaurants.isEmpty()) {

                return ResponseEntity
                        .status(404)
                        .body(
                        "No restaurant assigned today."
                        );

            }

            String restaurantName =
                    restaurants
                    .get(0)
                    .getName();

            RestaurantMenu menu =
                    menuRepository
                    .findByRestaurantName(
                            restaurantName
                    );

            if (menu == null) {

                return ResponseEntity
                        .status(404)
                        .body(
                        "Menu not found for today's restaurant."
                        );

            }

            return ResponseEntity.ok(
                    menu
            );

        }

        catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body(
                    "Error fetching today's menu."
                    );

        }

    }

    // ================================
    // Delete Single Menu Item
    // ================================

    @DeleteMapping("/delete-item")
    public ResponseEntity<?> deleteMenuItem(
            @RequestParam String restaurantName,
            @RequestParam String itemName) {

        try {

            RestaurantMenu menu =
                    menuRepository
                    .findByRestaurantName(
                            restaurantName
                    );

            if (menu == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                        "Menu not found"
                        );

            }

            menu.getMenuItems()
                    .removeIf(
                            item ->
                            item.getName()
                            .equalsIgnoreCase(
                                    itemName
                            )
                    );

            menuRepository.save(
                    menu
            );

            return ResponseEntity.ok(
                    "Item deleted successfully"
            );

        }

        catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body(
                    "Error deleting item"
                    );

        }

    }

    // ================================
    // Update Scanner Only
    // ================================

    @PutMapping("/update-scanner")
    public ResponseEntity<?> updateScanner(
            @RequestParam String restaurantName,
            @RequestParam String upiImageUrl) {

        try {

            RestaurantMenu menu =
                    menuRepository
                    .findByRestaurantName(
                            restaurantName
                    );

            if (menu == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                        "Menu not found"
                        );

            }

            menu.setUpiImageUrl(
                    upiImageUrl
            );

            menuRepository.save(
                    menu
            );

            return ResponseEntity.ok(
                    "Scanner updated successfully"
            );

        }

        catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body(
                    "Error updating scanner"
                    );

        }

    }

}