package com.projects.money_map_api.service;

import com.projects.money_map_api.dto.request.AccountRequest;
import com.projects.money_map_api.dto.response.AccountResponse;
import com.projects.money_map_api.entity.User;
import org.springframework.data.domain.Page;

import javax.security.auth.login.AccountException;

public interface AccountService {
    AccountResponse createAccount(User user, AccountRequest accountRequest) throws AccountException;

    AccountResponse editAccount(User user, String accountId, AccountRequest accountRequest) throws AccountException;


    AccountResponse viewAccount(User user, String accountId) throws AccountException;

    Page<AccountResponse> viewAccounts(User user, int page, int size);

    String deleteAccount(User user, String accountId);
}
