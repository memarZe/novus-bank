package com.novus.novusbank.transaction.services;

import java.util.List;

import com.novus.novusbank.response.Response;
import com.novus.novusbank.transaction.dto.TransactionDTO;
import com.novus.novusbank.transaction.dto.TransactionRequest;

public interface TransactionService {
    Response<?> createTransaction(TransactionRequest transactionRequest);

    Response<List<TransactionDTO>> getTransactionsForMyAccount(String accountNumber, int page, int size);
}