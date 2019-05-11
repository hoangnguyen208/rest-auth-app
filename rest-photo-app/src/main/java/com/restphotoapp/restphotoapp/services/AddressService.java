package com.restphotoapp.restphotoapp.services;

import java.util.ArrayList;
import java.util.List;

import com.restphotoapp.restphotoapp.exceptions.UserServiceException;
import com.restphotoapp.restphotoapp.models.DAL.Address;
import com.restphotoapp.restphotoapp.models.DAL.User;
import com.restphotoapp.restphotoapp.models.DTO.AddressResponse;
import com.restphotoapp.restphotoapp.models.Enum.ErrorMessages;
import com.restphotoapp.restphotoapp.repositories.IAddressRepository;
import com.restphotoapp.restphotoapp.repositories.IUserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService implements IAddressService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IAddressRepository addressRepository;

    @Override
    public List<AddressResponse> getAddresses(String userId) {
        List<AddressResponse> addressList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        User user = userRepository.findByUserId(userId);
        if (user == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        Iterable<Address> addresses = addressRepository.findAllByUser(user);

        if (addresses != null) {
            for (Address address : addresses) {
                addressList.add(modelMapper.map(address, AddressResponse.class));
            }
        }

        return addressList;
    }

    @Override
    public AddressResponse getAddress(String userId, String addressId) {
        User user = userRepository.findByUserId(userId);
        if (user == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        ModelMapper modelMapper = new ModelMapper();

        Address address = addressRepository.findByAddressId(addressId);

        AddressResponse addressToReturn = modelMapper.map(address, AddressResponse.class);

        return addressToReturn;
    }

}