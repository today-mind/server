package com.example.todaymindserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todaymindserver.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
