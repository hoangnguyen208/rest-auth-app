package com.restphotoapp.restphotoapp.models.DTO;

public class PasswordResetRequest {
    private String email;

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
}