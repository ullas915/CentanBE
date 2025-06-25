package com.example.UserSpecificChatBot.repository;

import com.example.UserSpecificChatBot.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}