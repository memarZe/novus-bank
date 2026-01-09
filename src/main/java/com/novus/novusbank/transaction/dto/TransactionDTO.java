package com.novus.novusbank.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.novus.novusbank.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.novus.novusbank.account.dto.AccountDTO;
import com.novus.novusbank.enums.TransactionStatus;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long id;

    private BigDecimal amount;

    private TransactionType transactionType;
    private LocalDateTime transactionDate;

    private String description;

    private TransactionStatus status;

    @JsonBackReference
    private AccountDTO account;
    // for transfer
    private String sourceAccount;
    private String destinationAccount;
}
