package com.restphotoapp.restphotoapp.services;

import java.util.ArrayList;
import java.util.List;

import com.restphotoapp.restphotoapp.exceptions.UserServiceException;
import com.restphotoapp.restphotoapp.helpers.AmazonSES;
import com.restphotoapp.restphotoapp.helpers.Utils;
import com.restphotoapp.restphotoapp.models.DAL.PasswordResetTokenEntity;
import com.restphotoapp.restphotoapp.models.DAL.User;
import com.restphotoapp.restphotoapp.models.DTO.AddressDTO;
import com.restphotoapp.restphotoapp.models.DTO.CreateUserRequest;
import com.restphotoapp.restphotoapp.models.DTO.UpdateUserRequest;
import com.restphotoapp.restphotoapp.models.DTO.UserDTO;
import com.restphotoapp.restphotoapp.models.DTO.UserDetailsResponse;
import com.restphotoapp.restphotoapp.models.Enum.ErrorMessages;
import com.restphotoapp.restphotoapp.repositories.IPasswordResetTokenRepository;
import com.restphotoapp.restphotoapp.repositories.IUserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AmazonSES amazonSES;

    @Autowired
    IPasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserDetailsResponse createUser(CreateUserRequest model) {
        User userFromRepo = userRepository.findByEmail(model.getEmail());
        ModelMapper modelMapper = new ModelMapper();

        if (userFromRepo != null)
            throw new UserServiceException("User already exists");

        UserDTO userDetail = modelMapper.map(model, UserDTO.class);
        userDetail.setEncryptedPassword(bCryptPasswordEncoder.encode(model.getPassword()));
        String publicUserId = utils.generateUserId(30);
        userDetail.setUserId(publicUserId);
        userDetail.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));

        for (AddressDTO a_req : model.getAddresses()) {
            a_req.setAddressId(utils.generateAddressId(30));
            a_req.setUserDetails(userDetail);
        }

        // User user = new User();
        // BeanUtils.copyProperties(model, user);
        User user = modelMapper.map(userDetail, User.class);
        User storedUser = userRepository.save(user);

        amazonSES.verifyEmail(storedUser);

        // UserDetailsResponse createdUser = new UserDetailsResponse();
        // BeanUtils.copyProperties(storedUser, createdUser);
        UserDetailsResponse createdUser = modelMapper.map(storedUser, UserDetailsResponse.class);

        return createdUser;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException(email);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPassword(),
                user.getEmailVerificationStatus(), true, true, true, new ArrayList<>());

        // return new
        // org.springframework.security.core.userdetails.User(user.getEmail(),
        // user.getEncryptedPassword(),
        // new ArrayList<>());
    }

    @Override
    public UserDetailsResponse getUser(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null)
            throw new UsernameNotFoundException(email);

        UserDetailsResponse returnedUser = new UserDetailsResponse();
        BeanUtils.copyProperties(user, returnedUser);

        return returnedUser;
    }

    @Override
    public UserDetailsResponse getUserById(String userId) {
        ModelMapper modelMapper = new ModelMapper();
        User user = userRepository.findByUserId(userId);
        if (user == null)
            throw new UsernameNotFoundException("User with ID " + userId + " not found");

        UserDetailsResponse userDetails = modelMapper.map(user, UserDetailsResponse.class);

        return userDetails;
    }

    @Override
    public UserDetailsResponse updateUser(String userId, UpdateUserRequest model) {
        ModelMapper modelMapper = new ModelMapper();

        User user = userRepository.findByUserId(userId);

        if (user == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        User updatedUser = userRepository.save(user);
        UserDetailsResponse userDetails = modelMapper.map(updatedUser, UserDetailsResponse.class);

        return userDetails;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findByUserId(userId);

        if (user == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(user);
    }

    @Override
    public List<UserDetailsResponse> getUsers(int page, int limit) {
        List<UserDetailsResponse> usersDetails = new ArrayList<>();

        if (page > 0)
            page = page - 1;

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<User> usersPage = userRepository.findAll(pageableRequest);
        List<User> users = usersPage.getContent();

        for (User user : users) {
            UserDetailsResponse userDetail = new UserDetailsResponse();
            BeanUtils.copyProperties(user, userDetail);
            usersDetails.add(userDetail);
        }

        return usersDetails;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        User user = userRepository.findUserByEmailVerificationToken(token);

        if (user != null) {
            boolean tokenHasExpired = Utils.hasExpiredToken(token);
            if (!tokenHasExpired) {
                user.setEmailVerificationToken(null);
                user.setEmailVerificationStatus(true);
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean requestPasswordReset(String email) {
        boolean result = false;
        User user = userRepository.findByEmail(email);

        if (user == null)
            return result;

        String token = new Utils().generatePasswordResetToken(user.getUserId());

        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUser(user);
        passwordResetTokenRepository.save(passwordResetTokenEntity);

        result = new AmazonSES().sendPasswordResetRequest(user.getFirstName(), user.getEmail(), token);

        return result;
    }

    @Override
    public boolean resetPassword(String token, String password) {
        if (Utils.hasExpiredToken(token))
            return false;

        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null)
            return false;

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        User user = passwordResetTokenEntity.getUser();
        user.setEncryptedPassword(encodedPassword);
        User updatedUser = userRepository.save(user);

        if (updatedUser != null && updatedUser.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            passwordResetTokenRepository.delete(passwordResetTokenEntity);
            return true;
        }

        return false;
    }

}