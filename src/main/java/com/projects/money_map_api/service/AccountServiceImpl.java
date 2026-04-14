package com.projects.money_map_api.service;

import com.projects.money_map_api.dto.request.AccountRequest;
import com.projects.money_map_api.dto.response.AccountResponse;
import com.projects.money_map_api.entity.Account;
import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.exception.MoneyMapException;
import com.projects.money_map_api.mapper.AccountMapper;
import com.projects.money_map_api.repository.AccountRepository;
import com.projects.money_map_api.utils.ErrorMessage;
import com.projects.money_map_api.utils.SuccessMessage;
import dev.langchain4j.model.chat.ChatModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.projects.money_map_api.utils.GeneralValidator.validateInput;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final ChatModel model;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;


    @Override
    public AccountResponse createAccount(User user, AccountRequest accountRequest) throws MoneyMapException {
        validateInput(accountRequest.getName(), "Account Name");
        validateInput(accountRequest.getCurrency(), "Currency");
        String prompt = String.format(
                "Analyze this financial account name: '%s' (Currency: %s). " +
                        "1. Provide a one-word category (e.g., Food, Transport, Family, Savings). " +
                        "2. Provide a very short, professional description of what this account is for. " +
                        "Format your response EXACTLY like this: Category | Description",
                accountRequest.getName(),
                accountRequest.getCurrency()
        );

        String aiResponse = model.chat(prompt);

        String[] parts = aiResponse.split("\\|");
        String category = parts[0].trim();
        String generatedDescription = (parts.length > 1) ? parts[1].trim() : "Automated account for " + accountRequest.getName();

        Account account = Account.builder()
                .user(user)
                .accountName(accountRequest.getName())
                .description(generatedDescription)
                .category(category)
                .currency(accountRequest.getCurrency())
                .dateCreated(LocalDateTime.now())
                .build();

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponse(savedAccount);
    }

    @Override
    public AccountResponse editAccount(User user, String accountId, AccountRequest accountRequest) throws MoneyMapException {
        if(StringUtils.isBlank(accountId)) {
            throw new MoneyMapException(ErrorMessage.ACCOUNT_ID_REQUIRED);
        }
        Account account = accountRepository.findAccountByIdAndUserId(accountId, user.getId())
                .orElseThrow(() -> new MoneyMapException(ErrorMessage.ACCOUNT_NOT_FOUND));
        account = accountMapper.toEntity(accountRequest, account);
        account.setDateUpdated(LocalDateTime.now());
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.toResponse(updatedAccount);
    }

    @Override
    public AccountResponse viewAccount(User user, String accountId) throws MoneyMapException {
        validateInput(accountId, "Account ID");
        Account foundAccount = accountRepository.findAccountByIdAndUserId(accountId, user.getId())
                .orElseThrow(() -> new MoneyMapException(ErrorMessage.ACCOUNT_NOT_FOUND));
        return accountMapper.toResponse(foundAccount);
    }

    @Override
    public Page<AccountResponse> viewAccounts(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accountsPage = accountRepository.findAllByUserId(user.getId(), pageable);
        return accountsPage.map(accountMapper::toResponse);
    }

    @Override
    public String deleteAccount(User user, String accountId){
        validateInput(accountId, "Account ID");
        if(accountRepository.existsAccountByIdAndUserId(accountId, user.getId())){
            accountRepository.deleteById(accountId);
        }
        return SuccessMessage.ACCOUNT_DELETED_SUCCESSFULLY;
    }

}
