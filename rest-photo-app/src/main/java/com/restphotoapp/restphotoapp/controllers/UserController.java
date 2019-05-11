package com.restphotoapp.restphotoapp.controllers;

import java.util.List;

import javax.validation.Valid;

import com.restphotoapp.restphotoapp.models.DTO.AddressResponse;
// import com.restphotoapp.restphotoapp.models.DAL.User;
import com.restphotoapp.restphotoapp.models.DTO.CreateUserRequest;
import com.restphotoapp.restphotoapp.models.DTO.OperationStatusModel;
import com.restphotoapp.restphotoapp.models.DTO.PasswordResetModel;
import com.restphotoapp.restphotoapp.models.DTO.PasswordResetRequest;
import com.restphotoapp.restphotoapp.models.DTO.UpdateUserRequest;
import com.restphotoapp.restphotoapp.models.DTO.UserDetailsResponse;
import com.restphotoapp.restphotoapp.models.Enum.RequestOperationName;
import com.restphotoapp.restphotoapp.models.Enum.RequestOperationStatus;
import com.restphotoapp.restphotoapp.services.IAddressService;
import com.restphotoapp.restphotoapp.services.IUserService;
// import com.restphotoapp.restphotoapp.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users") // http:localhost:8080/users
// @CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    IAddressService addressService;

    @ApiOperation(value = "Get User details Web Service enpoint", notes = "${userController.GetUser.ApiOperation.Notes}")
    // Configures authorization header in Swagger
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") })
    @GetMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserDetailsResponse getUser(@PathVariable String userId) {
        UserDetailsResponse userDetails = userService.getUserById(userId);
        return userDetails;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<UserDetailsResponse> createUser(@Valid @RequestBody CreateUserRequest model) {
        UserDetailsResponse createdUser = userService.createUser(model);

        return new ResponseEntity<UserDetailsResponse>(createdUser, HttpStatus.OK);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") })
    @PutMapping(path = "/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<UserDetailsResponse> updateUser(@PathVariable String userId,
            @Valid @RequestBody UpdateUserRequest model) {

        UserDetailsResponse updatedUser = userService.updateUser(userId, model);
        return new ResponseEntity<UserDetailsResponse>(updatedUser, HttpStatus.OK);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") })
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") })
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public List<UserDetailsResponse> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserDetailsResponse> usersList = userService.getUsers(page, limit);
        return usersList;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") })
    @GetMapping(path = "/{userId}/addresses", produces = { MediaType.APPLICATION_JSON_VALUE, "application/hal+json",
            MediaType.APPLICATION_XML_VALUE })
    public Resources<AddressResponse> getUserAddresses(@PathVariable String userId) {
        List<AddressResponse> addresses = addressService.getAddresses(userId);
        for (AddressResponse address : addresses) {
            Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, address.getAddressId()))
                    .withSelfRel();
            address.add(addressLink);

            Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
            address.add(userLink);
        }
        return new Resources<>(addresses);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") })
    @GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE,
            "application/hal+json", MediaType.APPLICATION_XML_VALUE })
    public Resource<AddressResponse> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
        AddressResponse address = addressService.getAddress(userId, addressId);

        // Link addressLink =
        // linkTo(UserController.class).slash(userId).slash("addresses").slash(addressId).withSelfRel();
        // equivalent with the below function
        Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
        Link userLink = linkTo(UserController.class).slash(userId).withRel("user");

        // Link addressesLink =
        // linkTo(UserController.class).slash(userId).slash("addresses").withRel("addresses");
        // equivalent with the below function
        Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");

        address.add(addressLink);
        address.add(userLink);
        address.add(addressesLink);
        return new Resource<>(address);
    }

    // users/email-verification?token=abcd
    @GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE, "application/hal+json",
            MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {
        OperationStatusModel value = new OperationStatusModel();
        value.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);
        if (isVerified) {
            value.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            value.setOperationResult(RequestOperationStatus.ERROR.name());
        }
        return value;
    }

    @PostMapping(path = "/password-reset-request", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequest model) {
        OperationStatusModel result = new OperationStatusModel();

        result.setOperationName(RequestOperationName.PASSWORD_RESET_REQUEST.name());

        boolean operationResult = userService.requestPasswordReset(model.getEmail());

        if (operationResult) {
            result.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            result.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return result;
    }

    @PostMapping(path = "/password-reset", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel model) {
        OperationStatusModel result = new OperationStatusModel();
        result.setOperationName(RequestOperationName.PASSWORD_RESET.name());

        boolean isPasswordUpdated = userService.resetPassword(model.getToken(), model.getPassword());

        if (isPasswordUpdated) {
            result.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            result.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return result;
    }

}