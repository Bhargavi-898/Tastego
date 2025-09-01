package com.svecw.tastego.repository;

import com.svecw.tastego.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {

    // Find a restaurant by the admin's email
    Restaurant findByAdminEmail(String email);

    // Find a restaurant by its name and code
    Restaurant findByName(String name);

    // Check if a restaurant with a specific code exists
    boolean existsByCode(String code);

    // Find a restaurant by code (returns Optional)
    Optional<Restaurant> findByCode(String code);

    // Find all restaurants by date
List<Restaurant> findByDate(LocalDate date); // ✅ Type must match the model
}
