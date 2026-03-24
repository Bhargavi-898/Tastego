package com.svecw.tastego.repository;

import com.svecw.tastego.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {

    // Find all restaurants by admin email
    List<Restaurant> findAllByAdminEmail(String adminEmail);

    // Find restaurant by name and code (login)
    Restaurant findByNameAndCode(String name, String code);

    // Find restaurant by name (used in ScannerImageController)
    Restaurant findByName(String name);

    // Find restaurants by date
    List<Restaurant> findByDate(LocalDate date);

    // Check if code already exists
    boolean existsByCode(String code);

    // Find restaurant by code
    Optional<Restaurant> findByCode(String code);
}