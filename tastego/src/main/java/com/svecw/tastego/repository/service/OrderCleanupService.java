package com.svecw.tastego.service;

import com.svecw.tastego.repository.OrderRepository;
import com.svecw.tastego.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderCleanupService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredRestaurantOrders(){

        LocalDate today =
                LocalDate.now();

        restaurantRepository
                .findAll()
                .forEach(restaurant -> {

            if(
                restaurant.getDate()
                .isBefore(today)
            ){

                orderRepository
                .deleteByRestaurantNameAndDateBefore(
                        restaurant.getName(),
                        today
                );

            }

        });

        System.out.println(
        "Expired restaurant orders deleted"
        );

    }

}