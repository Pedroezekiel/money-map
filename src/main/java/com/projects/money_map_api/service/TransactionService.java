package com.projects.money_map_api.service;

import com.projects.money_map_api.dto.request.TransactionRequest;
import com.projects.money_map_api.dto.response.TransactionResponse;
import com.projects.money_map_api.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionService {
    TransactionResponse createTransaction(User user, TransactionRequest request);

    TransactionResponse getTransactionById(User user, String transactionId);
}
