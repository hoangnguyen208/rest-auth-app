package com.restphotoapprestassured.restphotoapprestassured;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TestCreateUser {

    private final String CONTEXT_PATH = "/rest-photo-app";

    @BeforeEach
    void Setup() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    final void testCreateUser() {

        List<Map<String, Object>> userAddresses = new ArrayList<>();

        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("city", "Vancouver");
        shippingAddress.put("country", "Canada");
        shippingAddress.put("streetName", "123 Street name");
        shippingAddress.put("postalCode", "123456");
        shippingAddress.put("type", "shipping");

        Map<String, Object> billingAddress = new HashMap<>();
        billingAddress.put("city", "Vancouver");
        billingAddress.put("country", "Canada");
        billingAddress.put("streetName", "123 Street name");
        billingAddress.put("postalCode", "123456");
        billingAddress.put("type", "billing");

        userAddresses.add(shippingAddress);
        userAddresses.add(billingAddress);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Hng");
        userDetails.put("lastName", "Hng");
        userDetails.put("email", "hoangnguyen.vn208@gmail.com");
        userDetails.put("password", "test1234");
        userDetails.put("addresses", userAddresses);

        Response response = given().contentType("application/json").accept("application/json").body(userDetails).when()
                .post(CONTEXT_PATH + "/users").then().statusCode(200).contentType("application/json").extract()
                .response();

        String userId = response.jsonPath().getString("userId");
        assertNotNull(userId);

        String bodyString = response.body().asString();
        try {
            JSONObject responseBodyJson = new JSONObject(bodyString);
            JSONArray addresses = responseBodyJson.getJSONArray("addresses");
            assertNotNull(addresses);
            assertTrue(addresses.length() == 2);
            String addressId1 = addresses.getJSONObject(0).getString("addressId");
            assertNotNull(addressId1);
            assertTrue(addressId1.length() == 30);
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }
}