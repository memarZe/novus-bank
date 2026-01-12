package com.novus.novusbank.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novus.novusbank.account.services.AccountService;
import com.novus.novusbank.response.Response;

/**
 * AccountController handles HTTP requests related to user bank accounts.
 * <p>
 * Endpoints:
 * <ul>
 * <li><b>GET /api/accounts/me</b>: Retrieve all accounts for the authenticated
 * user</li>
 * <li><b>DELETE /api/accounts/close/{accountNumber}</b>: Close a specific
 * account by account number</li>
 * </ul>
 * <p>
 * This controller delegates business logic to the AccountService and returns
 * standardized responses.
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<Response<?>> getMyAccounts() {
        return ResponseEntity.ok(accountService.getMyAccounts());
    }

    @DeleteMapping("/close/{accountNumber}")
    public ResponseEntity<Response<?>> closeAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.closeAccount(accountNumber));
    }

}