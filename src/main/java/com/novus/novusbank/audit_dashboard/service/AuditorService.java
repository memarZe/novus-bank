package com.novus.novusbank.audit_dashboard.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.novus.novusbank.account.dto.AccountDTO;
import com.novus.novusbank.auth_users.dto.UserDTO;
import com.novus.novusbank.transaction.dto.TransactionDTO;

public interface AuditorService {

    Map<String, Long> getSystemTotals();

    Optional<UserDTO> findUserByEmail(String email);

    Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber);

    List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber);

    Optional<TransactionDTO> findTransactionById(Long transactionId);
}
