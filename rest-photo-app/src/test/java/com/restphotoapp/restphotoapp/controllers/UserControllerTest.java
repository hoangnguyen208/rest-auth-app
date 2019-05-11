package com.restphotoapp.restphotoapp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.restphotoapp.restphotoapp.models.DTO.AddressResponse;
import com.restphotoapp.restphotoapp.models.DTO.UserDetailsResponse;
import com.restphotoapp.restphotoapp.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    UserDetailsResponse userDetail;

    final String userId = "oidsjnfsdlkönca";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userDetail = new UserDetailsResponse();
        userDetail.setEmail("hoangnguyen.vn208@gmail.com");
        userDetail.setEmailVerificationStatus(false);
        userDetail.setFirstName("hng");
        userDetail.setLastName("hng");
        userDetail.setUserId(userId);
        userDetail.setAddresses(getAddressesDTO());
    }

    @Test
    final void testGetUser() {
        when(userService.getUserById(anyString())).thenReturn(userDetail);

        UserDetailsResponse userRes = userController.getUser(userId);
        assertNotNull(userRes);
        assertEquals(userId, userRes.getUserId());
        assertEquals(userDetail.getFirstName(), userRes.getFirstName());
        assertEquals(userDetail.getLastName(), userRes.getLastName());
        assertTrue(userDetail.getAddresses().size() == userRes.getAddresses().size());
    }

    private List<AddressResponse> getAddressesDTO() {
        AddressResponse shippingAddressDTO = new AddressResponse();
        shippingAddressDTO.setType("shipping");
        shippingAddressDTO.setCity("Gotham");
        shippingAddressDTO.setCountry("Wonderland");
        shippingAddressDTO.setPostalCode("123456");
        shippingAddressDTO.setStreetName("homeless 123");

        AddressResponse billingAddressDTO = new AddressResponse();
        billingAddressDTO.setType("billing");
        billingAddressDTO.setCity("Gotham");
        billingAddressDTO.setCountry("Wonderland");
        billingAddressDTO.setPostalCode("123456");
        billingAddressDTO.setStreetName("homeless 123");

        List<AddressResponse> addresses = new ArrayList<>();
        addresses.add(shippingAddressDTO);
        addresses.add(billingAddressDTO);

        return addresses;
    }
}