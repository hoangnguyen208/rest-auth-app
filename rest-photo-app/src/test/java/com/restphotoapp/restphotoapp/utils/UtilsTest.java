package com.restphotoapp.restphotoapp.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.restphotoapp.restphotoapp.helpers.Utils;

import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {
    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    final void testGenerateUserId() {
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId2);

        assertTrue(userId.length() == 30);
        assertTrue(!userId.equalsIgnoreCase(userId2));
    }

    @Test
    final void testHasExpiredToken() {
        String token = utils.generateEmailVerificationToken("wqefsc3w4e");

        assertNotNull(token);
        boolean hasTokenExpired = Utils.hasExpiredToken(token);
        assertFalse(hasTokenExpired);
    }
}