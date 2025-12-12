package com.example.todaymindserver.repository;

import com.example.todaymindserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // [필수] 닉네임 중복 체크용
    Optional<User> findByNickname(String nickname);
}