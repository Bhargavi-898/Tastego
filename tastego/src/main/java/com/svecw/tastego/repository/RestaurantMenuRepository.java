package com.svecw.tastego.repository;

import com.svecw.tastego.model.RestaurantMenu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantMenuRepository extends MongoRepository<RestaurantMenu, String> {
    RestaurantMenu findByRestaurantName(String restaurantName);
}
