package com.projects.money_map_api.service;

import com.projects.money_map_api.dto.request.TransactionRequest;
import com.projects.money_map_api.dto.response.TransactionResponse;
import com.projects.money_map_api.entity.Account;
import com.projects.money_map_api.entity.Transaction;
import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.exception.MoneyMapException;
import com.projects.money_map_api.mapper.TransactionMapper;
import com.projects.money_map_api.repository.AccountRepository;
import com.projects.money_map_api.repository.TransactionRepository;
import com.projects.money_map_api.utils.ErrorMessage;
import dev.langchain4j.model.chat.ChatModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.projects.money_map_api.utils.GeneralValidator.validateInput;


@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements  TransactionService {

    private final ChatModel model;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    @Override
    public TransactionResponse createTransaction(User user, TransactionRequest request){
        validateInput("Account Id", request.getAccountId());
        boolean needCategory = StringUtils.isBlank(request.getCategory());
        boolean needDescription = StringUtils.isBlank(request.getDescription());
        Account account = accountRepository.findAccountByIdAndUserId(request.getAccountId(), user.getId())
                .orElseThrow(() -> new MoneyMapException(ErrorMessage.ACCOUNT_NOT_FOUND));
        String category = request.getCategory();
        String description = request.getDescription();
        if (needCategory || needDescription) {
            String prompt = buildCreateTransactionPromptForDescriptionAndCategory(request, account);
            String aiResponse = model.chat(prompt);
            String[] parts = aiResponse.split("\\|");
            category = parts[0].trim();
            description = (parts.length > 1) ? parts[1].trim() : "Transaction for " + account.getAccountName();
        }
        Transaction transaction = Transaction.builder().account(account).user(user)
                .amount(request.getAmount())
                .type(request.getType())
                .description(description)
                .category(category)
                .dateCreated(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);
        switch (transaction.getType()) {
            case INCOME -> account.setSavingBalance(account.getSavingBalance().add(transaction.getAmount()));
            case EXPENSE -> account.setSpendingBalance(account.getSpendingBalance().add(transaction.getAmount()));
            case SAVING -> account.setSavingBalance(account.getSavingBalance().subtract(transaction.getAmount()));
        }
        accountRepository.save(account);
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    @Override
    public TransactionResponse getTransactionById(User user, String transactionId) {
        validateInput("Transaction Id", transactionId);
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, user.getId())
                .orElseThrow(() -> new MoneyMapException(ErrorMessage.TRANSACTION_NOT_FOUND));
        return transactionMapper.toResponse(transaction);
    }



    private String buildCreateTransactionPromptForDescriptionAndCategory(TransactionRequest request, Account account) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze this financial transaction:\n");
        prompt.append("- Amount: ").append(request.getAmount()).append("\n");
        prompt.append("- Type: ").append(request.getType()).append("\n");
        prompt.append("- Account: ").append(account.getAccountName()).append("\n");

        if (StringUtils.isNotBlank(request.getDescription())) {
            prompt.append("- User description: \"").append(request.getDescription()).append("\"\n");
        }
        if (StringUtils.isNotBlank(request.getCategory())) {
            prompt.append("- User category: \"").append(request.getCategory()).append("\"\n");
        }

        prompt.append("\n");
        prompt.append("Your task:\n");
        prompt.append("1. If description is missing, generate a concise, professional description (max 5 words).\n");
        prompt.append("2. If category is missing, assign a single-word category from: Food, Transport, Shopping, Bills, Entertainment, Health, Education, Income, Transfer, Other.\n");
        prompt.append("3. If both are present, just return them exactly as provided.\n");
        prompt.append("\n");
        prompt.append("Respond EXACTLY in this format: \"Category | Description\"\n");
        prompt.append("Example: \"Food | Lunch at Panera Bread\"");

        return prompt.toString();
    }


}
