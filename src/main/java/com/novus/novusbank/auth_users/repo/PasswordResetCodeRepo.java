package com.novus.novusbank.auth_users.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novus.novusbank.auth_users.entity.PasswordResetCode;

public interface PasswordResetCodeRepo extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByCode(String code);

    void deleteByUserId(Long userId);

}
