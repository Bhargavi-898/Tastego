package com.svecw.tastego.repository;

import com.svecw.tastego.model.ScannerImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScannerImageRepository extends MongoRepository<ScannerImage, String> {

    // ✅ match new field name
    List<ScannerImage> findByRestaurantName(String restaurantName);
}