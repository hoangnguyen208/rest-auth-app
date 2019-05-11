package com.restphotoapp.restphotoapp.repositories;

import java.util.List;

import com.restphotoapp.restphotoapp.models.DAL.Address;
import com.restphotoapp.restphotoapp.models.DAL.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressRepository extends CrudRepository<Address, Long> {
    List<Address> findAllByUser(User user);

    Address findByAddressId(String addressId);
}