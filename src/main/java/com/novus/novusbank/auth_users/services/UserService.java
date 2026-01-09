package com.novus.novusbank.auth_users.services;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.novus.novusbank.auth_users.dto.UpdatePasswordRequest;
import com.novus.novusbank.auth_users.dto.UserDTO;
import com.novus.novusbank.auth_users.entity.User;
import com.novus.novusbank.response.Response;

public interface UserService {

    User getCurrentLoggedInUser();

    Response<UserDTO> getMyProfile();

    Response<Page<UserDTO>> getAllUsers(int page, int size);

    Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);

    Response<?> uploadProfilePicture(MultipartFile file);

    Response<?> uploadProfilePictureToS3(MultipartFile file);

}
