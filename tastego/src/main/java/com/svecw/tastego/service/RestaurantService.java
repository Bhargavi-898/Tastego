package com.svecw.tastego.service;

import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    // Add a new restaurant with a unique code
    public Restaurant addRestaurant(Restaurant restaurant) {
        restaurant.setCode(generateUniqueCode());
        return repository.save(restaurant);
    }

    // Generate a unique restaurant code
    private String generateUniqueCode() {
        String code;
        do {
            code = "R" + new Random().nextInt(99999);
        } while (repository.existsByCode(code));
        return code;
    }

    // Fetch restaurants available today
    public List<Restaurant> getTodayRestaurants() {
        LocalDate today = LocalDate.now();  // get current date
        return repository.findByDate(today);
    }
}