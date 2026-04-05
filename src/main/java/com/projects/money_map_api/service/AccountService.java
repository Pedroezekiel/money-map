package com.projects.money_map_api.service;

import com.projects.money_map_api.dto.request.AccountRequest;
import com.projects.money_map_api.dto.response.AccountResponse;
import com.projects.money_map_api.entity.Account;
import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;


    public AccountResponse createAccount(User user, AccountRequest accountRequest) {

    }

}
