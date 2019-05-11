package com.restphotoapp.restphotoapp.models.DTO;

import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private List<AddressDTO> addresses;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the addresses
     */
    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    /**
     * @return the emailVerificationStatus
     */
    public Boolean getEmailVerificationStatus() {
        return emailVerificationStatus;
    }

    /**
     * @param emailVerificationStatus the emailVerificationStatus to set
     */
    public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
    }

    /**
     * @return the emailVerificationToken
     */
    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    /**
     * @param emailVerificationToken the emailVerificationToken to set
     */
    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    /**
     * @return the encryptedPassword
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    /**
     * @param encryptedPassword the encryptedPassword to set
     */
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
}