package com.projects.money_map_api.repository;

import com.projects.money_map_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
