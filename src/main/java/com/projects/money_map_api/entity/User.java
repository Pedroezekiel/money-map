package com.projects.money_map_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
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
