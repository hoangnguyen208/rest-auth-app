package com.restphotoapp.restphotoapp.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import com.restphotoapp.restphotoapp.exceptions.UserServiceException;
import com.restphotoapp.restphotoapp.helpers.AmazonSES;
import com.restphotoapp.restphotoapp.helpers.Utils;
import com.restphotoapp.restphotoapp.models.DAL.Address;
import com.restphotoapp.restphotoapp.models.DAL.User;
import com.restphotoapp.restphotoapp.models.DTO.AddressDTO;
import com.restphotoapp.restphotoapp.models.DTO.CreateUserRequest;
import com.restphotoapp.restphotoapp.models.DTO.UserDetailsResponse;
import com.restphotoapp.restphotoapp.repositories.IUserRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    IUserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    AmazonSES amazonSES;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    String userId = "asdfd";
    String encryptedPassword = "asdasdas";
    User user;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setId(1L);
        user.setFirstName("hng");
        user.setLastName("hng");
        user.setUserId(userId);
        user.setEncryptedPassword(encryptedPassword);
        user.setEmail("hoangnguyen.vn208@gmail.com");
        user.setEmailVerificationToken("qweertyrety");
        user.setAddresses(getAddressesEntity());
    }

    @Test
    public final void testGetUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserDetailsResponse userDetail = userService.getUser("test@test.com");

        assertNotNull(userDetail);
        assertEquals("hng", userDetail.getFirstName());
    }

    @Test
    public final void testGetUser_UsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUser("test@test.com");
        });
    }

    @Test
    public final void testCreateUser_CreateUserServiceException() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        CreateUserRequest userRequest = new CreateUserRequest();

        userRequest.setAddresses(getAddressesDTO());
        userRequest.setFirstName("hng");
        userRequest.setLastName("hng");
        userRequest.setPassword("test1234");
        userRequest.setEmail("hoangnguyen.vn208@gmail.com");

        assertThrows(UserServiceException.class, () -> {
            userService.createUser(userRequest);
        });
    }

    @Test
    public final void testCreateUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("cwefdsfsd");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.doNothing().when(amazonSES).verifyEmail(any(User.class));

        CreateUserRequest userRequest = new CreateUserRequest();

        userRequest.setAddresses(getAddressesDTO());
        userRequest.setFirstName("hng");
        userRequest.setLastName("hng");
        userRequest.setPassword("test1234");
        userRequest.setEmail("hoangnguyen.vn208@gmail.com");

        UserDetailsResponse userDetail = userService.createUser(userRequest);
        assertNotNull(userDetail);
        assertEquals(user.getFirstName(), userDetail.getFirstName());
        assertEquals(user.getLastName(), userDetail.getLastName());
        assertNotNull(userDetail.getUserId());
        assertEquals(userDetail.getAddresses().size(), user.getAddresses().size());
        verify(utils, times(userDetail.getAddresses().size())).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("test1234");
        verify(userRepository, times(1)).save(any(User.class));
    }

    private List<AddressDTO> getAddressesDTO() {
        AddressDTO shippingAddressDTO = new AddressDTO();
        shippingAddressDTO.setType("shipping");
        shippingAddressDTO.setCity("Gotham");
        shippingAddressDTO.setCountry("Wonderland");
        shippingAddressDTO.setPostalCode("123456");
        shippingAddressDTO.setStreetName("homeless 123");

        AddressDTO billingAddressDTO = new AddressDTO();
        billingAddressDTO.setType("billing");
        billingAddressDTO.setCity("Gotham");
        billingAddressDTO.setCountry("Wonderland");
        billingAddressDTO.setPostalCode("123456");
        billingAddressDTO.setStreetName("homeless 123");

        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(shippingAddressDTO);
        addresses.add(billingAddressDTO);

        return addresses;
    }

    private List<Address> getAddressesEntity() {
        List<AddressDTO> addresses = getAddressesDTO();

        Type listType = new TypeToken<List<Address>>() {
        }.getType();

        return new ModelMapper().map(addresses, listType);
    }
}