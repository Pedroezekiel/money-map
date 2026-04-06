package com.projects.money_map_api.repository;

import com.projects.money_map_api.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(@Email(message = "Email should be valid") @NotBlank(message = "Email cannot be blank") String email);

    User findByEmail(@Email(message = "Email should be valid") String email);
}
