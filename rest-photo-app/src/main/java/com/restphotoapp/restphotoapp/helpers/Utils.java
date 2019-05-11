package com.restphotoapp.restphotoapp.helpers;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import com.restphotoapp.restphotoapp.websecurity.SecurityConstants;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    public String generateAddressId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            randomString.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(randomString);
    }

    public static boolean hasExpiredToken(String token) {
        boolean isExpired = false;

        try {
            Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
                    .getBody();

            Date tokenExpirationDate = claims.getExpiration();
            Date today = new Date();

            isExpired = tokenExpirationDate.before(today);
        } catch (ExpiredJwtException ex) {
            isExpired = true;
        }

        return isExpired;
    }

    private String generateToken(String userId, long expiration) {
        String token = Jwts.builder().setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();
        return token;
    }

    public String generateEmailVerificationToken(String userId) {
        return generateToken(userId, SecurityConstants.EXPIRATION_TIME);
    }

    public String generatePasswordResetToken(String userId) {
        return generateToken(userId, SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME);
    }
}