package org.scooter;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderActions {
    public static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";

    public Response createOrder(Order data) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(data)
                .when()
                .post("/api/v1/orders");

    }

    public Response getOrders(Order data){
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(data)
                .when()
                .get("/api/v1/orders");


    }
}
