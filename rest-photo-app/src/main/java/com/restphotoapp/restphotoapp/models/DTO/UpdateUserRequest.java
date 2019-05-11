package com.restphotoapp.restphotoapp.models.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateUserRequest {

    @NotNull(message = "First name cannot be empty")
    @Size(min = 2, message = "First name must be at least 2 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be empty")
    @Size(min = 2, message = "Last name must be at least 2 characters")
    private String lastName;

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
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