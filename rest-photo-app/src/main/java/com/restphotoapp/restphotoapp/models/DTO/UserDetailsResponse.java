package com.restphotoapp.restphotoapp.models.DTO;

import java.util.List;

public class UserDetailsResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean emailVerificationStatus;
    private List<AddressResponse> addresses;

    /**
     * @return the emailVerificationStatus
     */
    public Boolean getEmailVerificationStatus() {
        return emailVerificationStatus;
    }

    /**
     * @return the addresses
     */
    public List<AddressResponse> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(List<AddressResponse> addresses) {
        this.addresses = addresses;
    }

    /**
     * @param emailVerificationStatus the emailVerificationStatus to set
     */
    public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
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
}