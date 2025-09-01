package com.svecw.tastego.service;

import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository repository;

    public Restaurant addRestaurant(Restaurant restaurant) {
        restaurant.setCode(generateUniqueCode());
        return repository.save(restaurant);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = "R" + new Random().nextInt(99999);
        } while (repository.existsByCode(code));
        return code;
    }
}
