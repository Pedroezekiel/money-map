package com.projects.money_map_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    private String id;

    @ManyToOne
    private User user;

    private String accountName;

    private String category;

    private BigDecimal savingBalance;

    private BigDecimal spendingBalance;

    private String currency;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;
}
