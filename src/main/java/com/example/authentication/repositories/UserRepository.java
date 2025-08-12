package com.example.authentication.repositories;

import com.example.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramId(long id);

    @Query("SELECT u.recoveryEmail FROM User u where u.telegramId = :id")
    Optional<String> findEmailByTelegramId(@Param("id") long id);

    List<User> findByTelegramIdIn(List<Long> ids);
}
