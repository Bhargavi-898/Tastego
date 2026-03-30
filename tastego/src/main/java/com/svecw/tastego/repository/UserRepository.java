package com.svecw.tastego.repository;

import com.svecw.tastego.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import com.svecw.tastego.model.Order;
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
