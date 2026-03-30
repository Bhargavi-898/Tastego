package com.svecw.tastego.repository;

import com.svecw.tastego.model.OrderTiming;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderTimingRepository
        extends MongoRepository<OrderTiming, String> {
}