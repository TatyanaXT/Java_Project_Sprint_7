package org.scooter;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;

public class CourierActions {
    public static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";

    public String generateCourierLogin (){
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String partLogin = formater.format(date);

        return "loqin_"+partLogin;
    }

    @Step("Send POST request to /api/v1/courier")
    public Response createCourier(Courier data) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(data)
                .when()
                .post("/api/v1/courier");

    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response loginCourier(Courier data) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(data)
                .when()
                .post("/api/v1/courier/login");
    }

    public int getCourierId(Courier data) {
        Response response = loginCourier(data);
        if (response.statusCode() == 200) {
            return response.path("id");
        }
        return 0;
    }

    @Step("Send DELETE request to /api/v1/courier/")
    public void deleteCourier(int courierId){
        String data = String.format("{\"id\": \"%d\"}", courierId);
        given()
            .contentType(ContentType.JSON)
            .baseUri(BASE_URI)
            .body(data)
            .when()
            .delete("/api/v1/courier/" + courierId );


    }

}
