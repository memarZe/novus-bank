package com.novus.novusbank.auth_users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.novus.novusbank.auth_users.dto.UpdatePasswordRequest;
import com.novus.novusbank.auth_users.dto.UserDTO;
import com.novus.novusbank.auth_users.services.UserService;
import com.novus.novusbank.response.Response;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * UserController manages user-related endpoints such as profile retrieval,
 * password updates, and profile picture uploads.
 * <p>
 * Endpoints:
 * <ul>
 * <li><b>GET /api/users</b>: Retrieve paginated list of all users (ADMIN
 * only)</li>
 * <li><b>GET /api/users/me</b>: Get the profile of the authenticated user</li>
 * <li><b>PUT /api/users/update-password</b>: Update the password for the
 * authenticated user</li>
 * <li><b>PUT /api/users/profile-picture</b>: Upload a new profile picture for
 * the authenticated user</li>
 * </ul>
 * <p>
 * This controller delegates business logic to the UserService and returns
 * standardized responses.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<Page<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @GetMapping("/me")
    public ResponseEntity<Response<UserDTO>> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PutMapping("/update-password")
    public ResponseEntity<Response<?>> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return ResponseEntity.ok(userService.updatePassword(updatePasswordRequest));
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<Response<?>> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.uploadProfilePictureToS3(file));
    }

}
