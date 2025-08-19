package com.baisebreno.learning_spring_api;

import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import com.baisebreno.learning_spring_api.domain.service.KitchenRegistryService;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.baisebreno.learning_spring_api.util.DatabaseCleaner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.aspectj.lang.annotation.Before;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("/application-test.properties") //tells the spring to look at a specific properties file
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KitchenRegistryAPITestsIT {
    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleaner databaseCleaner;

    @Autowired
    KitchenRepository kitchenRepository;

    @BeforeEach
    public void setup () {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/kitchens";

        databaseCleaner.clearTables();
        prepareData();

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
                .body("", Matchers.hasSize(2))
                .body("title", Matchers.hasItems("American","Chinese"));
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

    @Test
    public void shouldReturnCorrectResponseAndStatus_WhenKitchenFind(){

        given()
                .pathParam("id", 2) // passes params on the url
                .accept(ContentType.JSON)
            .when()
                .get("/{id}") //  -> does a GET /kitchens/2
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo("American")); // checks the response body for a "title" : "American".
    }

    @Test
    public void shouldReturnStatus404_WhenKitchenFind(){

        given()
                .pathParam("id", 2222) // passes params on the url
                .accept(ContentType.JSON)
            .when()
                .get("/{id}") //  -> does a GET /kitchens/2
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /* Utils */
    private void prepareData(){
        Kitchen kitchen = new Kitchen();
        kitchen.setName("Chinese");
        kitchenRepository.save(kitchen);

        Kitchen kitchen2 = new Kitchen();
        kitchen2.setName("American");
        kitchenRepository.save(kitchen2);

    }
}
