package com.restphotoapp.restphotoapp.models.DTO;

public class PasswordResetModel {
    private String token;
    private String password;

    /**
     * @return the token
     */
    public String getToken() {
        return token;
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
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
}