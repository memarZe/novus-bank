package com.novus.novusbank.auth_users.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.novus.novusbank.account.dto.AccountDTO;
import com.novus.novusbank.role.entity.Role;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;
    @JsonIgnore
    private String password;

    private String profilePictureUrl;
    private boolean active;

    private List<Role> roles;
    @JsonManagedReference
    private List<AccountDTO> accounts;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
