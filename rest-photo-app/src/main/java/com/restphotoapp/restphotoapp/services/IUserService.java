package com.restphotoapp.restphotoapp.services;

import java.util.List;

import com.restphotoapp.restphotoapp.models.DTO.CreateUserRequest;
import com.restphotoapp.restphotoapp.models.DTO.UpdateUserRequest;
import com.restphotoapp.restphotoapp.models.DTO.UserDetailsResponse;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    UserDetailsResponse createUser(CreateUserRequest model);

    UserDetailsResponse getUser(String email);

    UserDetailsResponse getUserById(String userId);

    UserDetailsResponse updateUser(String userId, UpdateUserRequest model);

    void deleteUser(String userId);

    List<UserDetailsResponse> getUsers(int page, int limit);

    boolean verifyEmailToken(String token);

    boolean requestPasswordReset(String email);

    boolean resetPassword(String token, String password);
}