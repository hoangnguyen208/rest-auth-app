package com.restphotoapp.restphotoapp.models.DTO;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequest {
    @NotNull(message = "First name cannot be empty")
    @Size(min = 2, message = "First name must be at least 2 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be empty")
    @Size(min = 2, message = "Last name must be at least 2 characters")
    private String lastName;

    @NotNull(message = "Email cannot be empty")
    @Email
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    private String password;

    private List<AddressDTO> addresses;

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
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
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}