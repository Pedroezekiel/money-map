package com.projects.money_map_api.repository;

import com.projects.money_map_api.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    boolean existsAccountByIdAndUserId(String accountId, String userId);

    Optional<Account> findAccountByIdAndUserId(String accountId, String userId);

    Page<Account> findAllByUserId(String id, Pageable pageable);
}
