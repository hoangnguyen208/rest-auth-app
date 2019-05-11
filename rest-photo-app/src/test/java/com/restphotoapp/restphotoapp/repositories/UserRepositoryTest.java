package com.restphotoapp.restphotoapp.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.restphotoapp.restphotoapp.models.DAL.Address;
import com.restphotoapp.restphotoapp.models.DAL.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    IUserRepository userRepository;

    static boolean recordedCreated = false;

    @BeforeEach
    void setUp() throws Exception {
        if (!recordedCreated)
            createRecords();
    }

    @Test
    final void testGetVerifiedUsers() {
        Pageable pageableRequest = PageRequest.of(1, 1);

        Page<User> page = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
        assertNotNull(page);

        List<User> users = page.getContent();
        assertNotNull(users);
        assertTrue(users.size() == 1);
    }

    @Test
    final void testFindUsersByFirstName() {
        String firstName = "Mimi";
        List<User> users = userRepository.findUsersByFirstName(firstName);

        assertNotNull(users);
        assertTrue(users.size() == 2);

        User user = users.get(0);
        assertTrue(user.getFirstName().equals(firstName));
    }

    @Test
    final void testFindUsersByLastName() {
        String lastName = "Mama";
        List<User> users = userRepository.findUsersByLastName(lastName);

        assertNotNull(users);
        assertTrue(users.size() == 2);

        User user = users.get(0);
        assertTrue(user.getLastName().equals(lastName));
    }

    @Test
    final void testFindUsersByKeyword() {
        String keyword = "Mi";
        List<User> users = userRepository.findUsersByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() == 2);

        User user = users.get(0);
        assertTrue(user.getLastName().contains(keyword) || user.getFirstName().contains(keyword));
    }

    @Test
    final void testFindUsersFirstNameAndLastNameByKeyword() {
        String keyword = "Mi";
        List<Object[]> users = userRepository.findUsersFirstNameAndLastNameByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() == 2);

        Object[] user = users.get(0);

        assertTrue(user.length == 2);

        // because this query in the root method 'select u.first_name' takes 1st, then
        // user[0]
        // would be user first name
        String userFirstName = String.valueOf(user[0]);
        String userLastName = String.valueOf(user[1]);

        assertNotNull(userFirstName);
        assertNotNull(userLastName);

        System.out.println("First name = " + userFirstName);
        System.out.println("Last name = " + userLastName);
    }

    @Test
    final void testUpdateUserEmailVerificationStatus() {
        boolean newEmailVerificationStatus = false;

        userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "123abc");

        User user = userRepository.findByUserId("123abc");

        boolean storedEmailVerificationStatus = user.getEmailVerificationStatus();

        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
    }

    @Test
    final void testFindUserEntityByUserId() {
        String userId = "123abc";
        User user = userRepository.findUserEntityByUserId(userId);

        assertNotNull(user);
        assertTrue(user.getUserId().equals(userId));
    }

    @Test
    final void testGetUserFullNameById() {
        String userId = "123abc";
        List<Object[]> records = userRepository.getUserFullNameById(userId);

        assertNotNull(records);
        assertTrue(records.size() == 1);

        Object[] userDetails = records.get(0);

        String firstName = String.valueOf(userDetails[0]);
        String lastName = String.valueOf(userDetails[1]);

        assertNotNull(firstName);
        assertNotNull(lastName);
    }

    @Test
    final void testUpdateUserEntityEmailVerificationStatus() {
        boolean newEmailVerificationStatus = false;

        userRepository.updateUserEntityEmailVerificationStatus(newEmailVerificationStatus, "123abc");

        User user = userRepository.findByUserId("123abc");

        boolean storedEmailVerificationStatus = user.getEmailVerificationStatus();

        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
    }

    private void createRecords() {
        User user = new User();
        user.setFirstName("Mimi");
        user.setLastName("Mama");
        user.setUserId("123abc");
        user.setEncryptedPassword("encryptedPassword");
        user.setEmail("test@test.com");
        user.setEmailVerificationStatus(true);

        Address address = new Address();
        address.setAddressId("addressId");
        address.setCity("city");
        address.setCountry("country");
        address.setPostalCode("postalCode");
        address.setStreetName("streetName");
        address.setType("type");

        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        user.setAddresses(addresses);

        userRepository.save(user);

        User user2 = new User();
        user2.setFirstName("Mimi");
        user2.setLastName("Mama");
        user2.setUserId("123def");
        user2.setEncryptedPassword("encryptedPassword");
        user2.setEmail("test@test1.com");
        user2.setEmailVerificationStatus(true);

        Address address2 = new Address();
        address2.setAddressId("addressId2");
        address2.setCity("city");
        address2.setCountry("country");
        address2.setPostalCode("postalCode");
        address2.setStreetName("streetName");
        address2.setType("type");

        List<Address> addresses2 = new ArrayList<>();
        addresses2.add(address2);
        user2.setAddresses(addresses2);

        userRepository.save(user2);

        recordedCreated = true;
    }
}