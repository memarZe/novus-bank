package com.novus.novusbank.transaction.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.novus.novusbank.enums.TransactionType;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    private TransactionType transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String description;
    private String destinationAccountNumber;

}
