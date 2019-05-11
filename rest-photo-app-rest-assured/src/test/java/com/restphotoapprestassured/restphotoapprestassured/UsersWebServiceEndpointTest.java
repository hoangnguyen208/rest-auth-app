package com.restphotoapprestassured.restphotoapprestassured;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UsersWebServiceEndpointTest {
    private final String CONTEXT_PATH = "/rest-photo-app";
    private final String EMAIL_ADDRESS = "hoangnguyen.vn208@gmail.com";
    private final String PASSWORD = "test1234";
    private final String JSON = "application/json";
    private static String authorizationHeader;
    private static String userId;
    private static List<Map<String, String>> addresses;

    @BeforeEach
    void Setup() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    // Test Login
    @Test
    final void a() {
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", EMAIL_ADDRESS);
        loginDetails.put("password", PASSWORD);

        Response response = given().contentType(JSON).accept(JSON).body(loginDetails).when()
                .post(CONTEXT_PATH + "/users/login").then().statusCode(200).extract().response();

        authorizationHeader = response.header("Authorization");
        userId = response.header("UserID");

        assertNotNull(authorizationHeader);
        assertNotNull(userId);
    }

    // Test Get User
    @Test
    final void b() {
        Response response = given().pathParam("id", userId).header("Authorization", authorizationHeader).accept(JSON)
                .when().get(CONTEXT_PATH + "/users/{id}").then().statusCode(200).contentType(JSON).extract().response();

        String userPubId = response.jsonPath().getString("userId");
        String userEmail = response.jsonPath().getString("email");
        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");

        addresses = response.jsonPath().getList("addresses");
        String addressId1 = addresses.get(0).get("addressId");

        assertNotNull(userPubId);
        assertNotNull(userEmail);
        assertNotNull(firstName);
        assertNotNull(lastName);
        assertEquals(EMAIL_ADDRESS, userEmail);

        assertTrue(addresses.size() == 2);
        assertTrue(addressId1.length() == 30);
    }

    // Test Update User
    @Test
    final void c() {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Hoang");
        userDetails.put("lastName", "Ng");

        Response response = given().contentType(JSON).accept(JSON).header("Authorization", authorizationHeader)
                .pathParam("id", userId).body(userDetails).when().put(CONTEXT_PATH + "/users/{id}").then()
                .statusCode(200).contentType(JSON).extract().response();

        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");

        List<Map<String, String>> updatedAddresses = response.jsonPath().getList("addresses");

        assertEquals("Hoang", firstName);
        assertEquals("Ng", lastName);
        assertNotNull(updatedAddresses);
        assertTrue(addresses.size() == updatedAddresses.size());
        assertEquals(addresses.get(0).get("streetName"), updatedAddresses.get(0).get("streetName"));
    }

    // Test Delete User
    @Test
    // @Disabled
    final void d() {
        given().header("Authorization", authorizationHeader).accept(JSON).pathParam("id", userId).when()
                .delete(CONTEXT_PATH + "/users/{id}").then().statusCode(204);
    }

}