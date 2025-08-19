package com.baisebreno.learning_spring_api;

import com.baisebreno.learning_spring_api.domain.service.KitchenRegistryService;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KitchenRegistryAPITestsIT {
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup () {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/kitchens";
    }
    @Autowired
    KitchenRegistryService kitchenRegistryService;

    @Autowired
    RestaurantRegistryService restaurantRegistryService;

    @Test
    void contextLoads() {

    }

    @Test
    public void shouldReturnStatus200_WhenGetAllKitchens(){
        given()
                .basePath("/kitchens")
                .port(port)
                .accept(ContentType.JSON)
            .when()
                .get()
            .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldContain4Kitchens_WhenGetAllKitchens(){
        given()
                .accept(ContentType.JSON)
            .when()
                .get()
            .then()
                .body("", Matchers.hasSize(4))
                .body("title", Matchers.hasItems("Argentinian","Brazilian"));
    }

    @Test
    public void shouldReturnStatus201_WhenAddingKitchen(){
        given()
                .body("{ \"title\": \"ChaoMein\" }")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
            .when()
                .post()
            .then()
                .statusCode(HttpStatus.CREATED.value());
    }
}
