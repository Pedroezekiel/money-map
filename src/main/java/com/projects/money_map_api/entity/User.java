package com.projects.money_map_api.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String password;
    private String profileImageUrl;
}
