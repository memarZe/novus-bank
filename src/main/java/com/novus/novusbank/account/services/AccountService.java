package com.novus.novusbank.account.services;

import java.util.List;

import com.novus.novusbank.account.dto.AccountDTO;
import com.novus.novusbank.account.entity.Account;
import com.novus.novusbank.auth_users.entity.User;
import com.novus.novusbank.enums.AccountType;
import com.novus.novusbank.response.Response;

public interface AccountService {

    Account createAccount(AccountType accountType, User user);

    Response<List<AccountDTO>> getMyAccounts();

    Response<?> closeAccount(String accountNumber);
}
