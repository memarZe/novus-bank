package com.novus.novusbank.auth_users.services.impl;

import com.novus.novusbank.account.entity.Account;
import com.novus.novusbank.account.services.AccountService;
import com.novus.novusbank.auth_users.dto.LoginRequest;
import com.novus.novusbank.auth_users.dto.LoginResponse;
import com.novus.novusbank.auth_users.dto.RegistrationRequest;
import com.novus.novusbank.auth_users.dto.ResetPasswordRequest;
import com.novus.novusbank.auth_users.entity.PasswordResetCode;
import com.novus.novusbank.enums.AccountType;
import com.novus.novusbank.enums.Currency;
import com.novus.novusbank.exceptions.BadRequestException;
import com.novus.novusbank.exceptions.NotFoundException;
import com.novus.novusbank.notification.dto.NotificationDTO;
import com.novus.novusbank.notification.services.NotificationService;
import com.novus.novusbank.response.Response;
import com.novus.novusbank.role.entity.Role;
import com.novus.novusbank.role.repo.RoleRepo;
import com.novus.novusbank.security.TokenService;
import com.novus.novusbank.auth_users.entity.User;
import com.novus.novusbank.auth_users.repo.PasswordResetCodeRepo;
import com.novus.novusbank.auth_users.repo.UserRepo;
import com.novus.novusbank.auth_users.services.AuthService;
import com.novus.novusbank.auth_users.services.CodeGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

        private final UserRepo userRepo;
        private final RoleRepo roleRepo;
        private final PasswordEncoder passwordEncoder;
        private final TokenService tokenService;
        private final NotificationService notificationService;
        private final AccountService accountService;

        private final CodeGenerator codeGenerator;
        private final PasswordResetCodeRepo passwordResetCodeRepo;

        @Value("${password.reset.link}")
        private String resetLink;

        @Override
        public Response<String> register(RegistrationRequest request) {

                List<Role> roles;

                if (request.getRoles() == null || request.getRoles().isEmpty()) {
                        // DEFAULT TO CUSTOMER
                        Role defaultRole = roleRepo.findByName("CUSTOMER")
                                        .orElseThrow(() -> new NotFoundException("CUSTOMER ROLE NOT FOUND"));

                        roles = Collections.singletonList(defaultRole);
                } else {
                        roles = request.getRoles().stream()
                                        .map(roleName -> roleRepo.findByName(roleName)
                                                        .orElseThrow(() -> new NotFoundException(
                                                                        "ROLE NOT FOUND" + roleName)))
                                        .toList();
                }

                if (userRepo.findByEmail(request.getEmail()).isPresent()) {
                        throw new BadRequestException("Email Already Present");
                }

                User user = User.builder()
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .email(request.getEmail())
                                .phoneNumber(request.getPhoneNumber())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(roles)
                                .active(true)
                                .build();

                User savedUser = userRepo.save(user);

                // Create ACCOUNT NUMBERT FOR THE USER
                Account savedAccount = accountService.createAccount(AccountType.SAVINGS, savedUser);

                // SEND WELCOME EMAIL
                Map<String, Object> vars = new HashMap<>();
                vars.put("name", savedUser.getFirstName());

                NotificationDTO notificationDTO = NotificationDTO.builder()
                                .recipient(savedUser.getEmail())
                                .subject("Welcome to Phegon Bank 🎉")
                                .templateName("welcome")
                                .templateVariables(vars)
                                .build();

                notificationService.sendEmail(notificationDTO, savedUser);

                // SEND ACCOUNT CREATION/DETAILS EMAIL
                Map<String, Object> accountVars = new HashMap<>();
                accountVars.put("name", savedUser.getFirstName());
                accountVars.put("accountNumber", savedAccount.getAccountNumber());
                accountVars.put("accountType", AccountType.SAVINGS.name());
                accountVars.put("currency", Currency.USD);

                NotificationDTO accountCreatedEmail = NotificationDTO.builder()
                                .recipient(savedUser.getEmail())
                                .subject("Your New Bank Account Has Been Created ✅")
                                .templateName("account-created")
                                .templateVariables(accountVars)
                                .build();

                notificationService.sendEmail(accountCreatedEmail, savedUser);

                return Response.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Your account has been created successfully")
                                .data("Email of your account details has been sent to you. Your account number is: "
                                                + savedAccount.getAccountNumber())
                                .build();
        }

        @Override
        public Response<LoginResponse> login(LoginRequest loginRequest) {

                String email = loginRequest.getEmail();
                String password = loginRequest.getPassword();

                User user = userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("Email Not Found"));

                if (!passwordEncoder.matches(password, user.getPassword())) {
                        throw new BadRequestException("Password doesn't match");
                }

                String token = tokenService.generateToken(user.getEmail());

                LoginResponse loginResponse = LoginResponse.builder()
                                .roles(user.getRoles().stream().map(Role::getName).toList())
                                .token(token)
                                .build();

                return Response.<LoginResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Login Successful")
                                .data(loginResponse)
                                .build();
        }

        @Override
        @Transactional
        public Response<?> forgetPassword(String email) {

                User user = userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User Not Found"));
                passwordResetCodeRepo.deleteByUserId(user.getId());

                String code = codeGenerator.generateUniqueCode();

                PasswordResetCode resetCode = PasswordResetCode.builder()
                                .user(user)
                                .code(code)
                                .expiryDate(calculateExpiryDate())
                                .used(false)
                                .build();

                passwordResetCodeRepo.save(resetCode);

                // send email reset link out
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", user.getFirstName());
                templateVariables.put("resetLink", resetLink + code);

                NotificationDTO notificationDTO = NotificationDTO.builder()
                                .recipient(user.getEmail())
                                .subject("Password Reset Code")
                                .templateName("password-reset")
                                .templateVariables(templateVariables)
                                .build();

                notificationService.sendEmail(notificationDTO, user);

                return Response.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Password reset code sent to your email")
                                .build();

        }

        @Override
        @Transactional
        public Response<?> updatePasswordViaResetCode(ResetPasswordRequest resetPasswordRequest) {
                String code = resetPasswordRequest.getCode();
                String newPassword = resetPasswordRequest.getNewPassword();

                // Find and validate code

                PasswordResetCode resetCode = passwordResetCodeRepo.findByCode(code)
                                .orElseThrow(() -> new BadRequestException("Invalid reset code"));

                // Check expiration first
                if (resetCode.getExpiryDate().isBefore(LocalDateTime.now())) {
                        passwordResetCodeRepo.delete(resetCode); // Clean up expired code
                        throw new BadRequestException("Reset code has expired");
                }

                // update the pasword
                User user = resetCode.getUser();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepo.save(user);

                // Delete the code immediately after successful use
                passwordResetCodeRepo.delete(resetCode);

                // Send confirmation email
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", user.getFirstName());

                NotificationDTO confirmationEmail = NotificationDTO.builder()
                                .recipient(user.getEmail())
                                .subject("Password Updated Successfully")
                                .templateName("password-update-confirmation")
                                .templateVariables(templateVariables)
                                .build();

                notificationService.sendEmail(confirmationEmail, user);

                return Response.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Password updated successfully")
                                .build();
        }

        private LocalDateTime calculateExpiryDate() {
                return LocalDateTime.now().plusHours(5);
        }
}