package com.novus.novusbank.auth_users.services;

import com.novus.novusbank.auth_users.dto.LoginRequest;
import com.novus.novusbank.auth_users.dto.LoginResponse;
import com.novus.novusbank.auth_users.dto.RegistrationRequest;
import com.novus.novusbank.auth_users.dto.ResetPasswordRequest;
import com.novus.novusbank.response.Response;

public interface AuthService {

    Response<String> register(RegistrationRequest request);

    Response<LoginResponse> login(LoginRequest loginRequest);

    Response<?> forgetPassword(String email);

    Response<?> updatePasswordViaResetCode(ResetPasswordRequest request);
}
