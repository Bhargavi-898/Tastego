package com.svecw.tastego.repository;

import com.svecw.tastego.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository 
        extends MongoRepository<Restaurant, String> {

    // Find all restaurants added by a specific admin
    List<Restaurant> findAllByAdminEmail(String adminEmail);

    // Login using restaurant name and code
    Optional<Restaurant> findByNameAndCode(
            String name,
            String code
    );

    // Find restaurants available on a specific date
    List<Restaurant> findByDate(LocalDate date);

    // Check if restaurant code already exists
    boolean existsByCode(String code);

    // Find restaurant by unique code
    Optional<Restaurant> findByCode(String code);

    // Find restaurant by name
    Optional<Restaurant> findByName(String name);

    // Delete restaurants before a given date (for daily cleanup)
    void deleteByDateBefore(LocalDate date);

}