package com.novus.novusbank.auth_users.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.novus.novusbank.auth_users.dto.UpdatePasswordRequest;
import com.novus.novusbank.auth_users.dto.UserDTO;
import com.novus.novusbank.auth_users.entity.User;
import com.novus.novusbank.auth_users.repo.UserRepo;
import com.novus.novusbank.auth_users.services.UserService;
import com.novus.novusbank.exceptions.BadRequestException;
import com.novus.novusbank.exceptions.NotFoundException;
import com.novus.novusbank.notification.dto.NotificationDTO;
import com.novus.novusbank.notification.services.NotificationService;
import com.novus.novusbank.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.data.domain.Page;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final String uploadeDir = "upload/profile-pictures/";

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new NotFoundException("User is not authenticated");
        }

        String email = authentication.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public Response<UserDTO> getMyProfile() {
        User user = getCurrentLoggedInUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User profile retrieved successfully")
                .data(userDTO)
                .build();
    }

    @Override
    public Response<Page<UserDTO>> getAllUsers(int page, int size) {
        Page<User> usersPage = userRepo.findAll(PageRequest.of(page, size));
        Page<UserDTO> userDTOsPage = usersPage.map(user -> modelMapper.map(user, UserDTO.class));

        return Response.<Page<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User list retrieved successfully")
                .data(userDTOsPage)
                .build();
    }

    @Override
    public Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = getCurrentLoggedInUser();
        String oldPassword = updatePasswordRequest.getOldPassword();
        String newPassword = updatePasswordRequest.getNewPassword();

        if (oldPassword == null || newPassword == null) {
            throw new BadRequestException("Old password and new password must be provided");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", user.getFirstName());

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Your Password Was Successfully Changed")
                .templateName("password-change")
                .templateVariables(templateVariables)
                .build();

        notificationService.sendEmail(notificationDTO, user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password changed successfully")
                .build();
    }

    @Override
    public Response<?> uploadProfilePicture(MultipartFile file) {
        User user = getCurrentLoggedInUser();

        try {
            Path uploadPath = Paths.get(uploadeDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
                Path oldFile = Paths.get(user.getProfilePictureUrl());

                if (Files.exists(oldFile)) {
                    Files.delete(oldFile);
                }
            }

            String orginalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (orginalFilename != null && orginalFilename.contains(".")) {
                fileExtension = orginalFilename.substring(orginalFilename.lastIndexOf("."));
            }

            String newFilename = UUID.randomUUID() + fileExtension;
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            String fileUrl = "profile-picture/" + newFilename;
            user.setProfilePictureUrl(fileUrl);
            user.setUpdatedAt(LocalDateTime.now());

            userRepo.save(user);

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile picture uploaded successfully")
                    .build();

        } catch (IOException e) {
            throw new RuntimeErrorException(null, "Failed to upload profile picture");
        }
    }

    @Override
    public Response<?> uploadProfilePictureToS3(MultipartFile file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadProfilePictureToS3'");
    }

}