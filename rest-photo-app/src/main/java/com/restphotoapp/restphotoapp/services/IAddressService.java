package com.restphotoapp.restphotoapp.services;

import java.util.List;

import com.restphotoapp.restphotoapp.models.DTO.AddressResponse;

public interface IAddressService {
    List<AddressResponse> getAddresses(String userId);

    AddressResponse getAddress(String userId, String addressId);
}