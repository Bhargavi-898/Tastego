package com.svecw.tastego.scheduler;

import com.svecw.tastego.repository.OrderRepository;
import com.svecw.tastego.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderCleanupScheduler {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private RestaurantRepository restaurantRepo;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteOldOrdersAndRestaurants(){

        LocalDate today = LocalDate.now();

        // Delete any order older than today (stale data)
        orderRepo.deleteByDateBefore(today);

        // Delete restaurants whose assignment date has passed
        restaurantRepo.deleteByDateBefore(today);

        System.out.println("Old orders and expired restaurants deleted successfully");
    }

}