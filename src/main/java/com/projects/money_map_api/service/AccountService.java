package com.projects.money_map_api.service;

import com.projects.money_map_api.dto.request.AccountRequest;
import com.projects.money_map_api.dto.response.AccountResponse;
import com.projects.money_map_api.entity.Account;
import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.repository.AccountRepository;
import com.projects.money_map_api.utils.ErrorMessage;
import dev.langchain4j.model.chat.ChatModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    private final ChatModel model;
    private final AccountRepository accountRepository;


    public AccountResponse createAccount(User user, AccountRequest accountRequest) throws AccountException {
        if(!user.isActive()){
            throw new AccountException(ErrorMessage.USER_IS_INACTIVE);
        }
        String description = (accountRequest.getDescription() != null)
                ? accountRequest.getDescription()
                : "No description provided";

        String prompt = String.format(
                "Analyze this transaction: Name: '%s', Info: '%s'. " +
                        "Select the best category from [Food, Transport, Utilities, Entertainment, Healthcare, Shopping]. " +
                        "If it doesn't fit, provide a specific one-word category (e.g., 'Family', 'Education'). " +
                        "Reply with ONLY the one-word category.",
                accountRequest.getName(),
                description
        );

        String category = model.chat(prompt);

        Account account = Account.builder()
                .accountName(accountRequest.getName()).description(accountRequest.getDescription())
                .category(category)
                .dateCreated(LocalDateTime.now())
                .build();

        Account savedAccount = accountRepository.save(account);
        return AccountResponse.builder().id(savedAccount.getId())
                .name(savedAccount.getAccountName()).category(savedAccount.getCategory()).savingBalance(savedAccount.getSavingBalance())
                .spendingBalance(savedAccount.getSpendingBalance()).currency(savedAccount.getCurrency())
                .build();
    }

}
