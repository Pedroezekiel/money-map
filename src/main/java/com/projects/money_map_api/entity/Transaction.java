package com.projects.money_map_api.entity;

import com.projects.money_map_api.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private String id;

    private BigDecimal amount;

    private TransactionType type;

    private String description;

    private LocalDateTime transactionDate;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String category;
}
