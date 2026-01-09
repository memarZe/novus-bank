package com.novus.novusbank.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.novus.novusbank.auth_users.entity.User;

import jakarta.annotation.Nullable;

public class AuthUser implements UserDetails {

    private User user;

    public static AuthUserBuilder builder() {
        return new AuthUserBuilder();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class AuthUserBuilder {
        private User user;

        public AuthUserBuilder user(User user) {
            this.user = user;
            return this;
        }

        public AuthUser build() {
            AuthUser authUser = new AuthUser();
            authUser.setUser(this.user);
            return authUser;
        }
    }
}