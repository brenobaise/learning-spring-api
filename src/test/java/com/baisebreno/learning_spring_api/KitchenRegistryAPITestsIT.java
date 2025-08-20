package com.baisebreno.learning_spring_api;

import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import com.baisebreno.learning_spring_api.domain.service.KitchenRegistryService;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.baisebreno.learning_spring_api.util.DatabaseCleaner;
import com.baisebreno.learning_spring_api.util.ResourceUtils;
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

import java.util.ArrayList;
import java.util.List;

@TestPropertySource("/application-test.properties") //tells the spring to look at a specific properties file
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KitchenRegistryAPITestsIT {
    @LocalServerPort
    private int port;

    private int COUNT_OF_KITCHENS_IN_DB;
    private final int NON_EXISTENT_KITCHEN_ID = 100;
    private final int EXISTENT_KITCHEN_ID = 2;
    private String TEST_JSON;

    @Autowired
    DatabaseCleaner databaseCleaner;

    @Autowired
    KitchenRepository kitchenRepository;

    @BeforeEach
    public void setup () {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/kitchens";

        TEST_JSON = ResourceUtils.getContentFromResource(
                "/json/test_json.json");

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
    public void shouldContainNKitchens_WhenGetAllKitchens(){
        given()
                .accept(ContentType.JSON)
            .when()
                .get()
            .then()
                .body("", Matchers.hasSize(COUNT_OF_KITCHENS_IN_DB));
    }

    @Test
    public void shouldReturnStatus201_WhenAddingKitchen(){
        given()
                .body(TEST_JSON)
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
                .pathParam("id", 4) // passes params on the url
                .accept(ContentType.JSON)
            .when()
                .get("/{id}") //  -> does a GET /kitchens/EXISTENT_KITCHEN_ID
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo("American")); // checks the response body for a "title" : "American".
    }

    @Test
    public void shouldReturnStatus404_WhenKitchenFind(){
        given()
                .pathParam("id", NON_EXISTENT_KITCHEN_ID) // passes params on the url
                .accept(ContentType.JSON)
            .when()
                .get("/{id}") //  -> does a GET /kitchens/2
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /* Utils */
    private void prepareData(){
        final int NUM_OF_KITCHENS = 3;
        List<Kitchen> kitchenList = new ArrayList<>();

        for (int i = 0; i < NUM_OF_KITCHENS; i++) {
            Kitchen kitchen = new Kitchen();
            kitchen.setName("kitchen " + i);
            kitchenList.add(kitchen);
        }

        Kitchen kitchen = new Kitchen();
        kitchen.setName("American");
        kitchenList.add(kitchen);

        kitchenRepository.saveAll(kitchenList);

        COUNT_OF_KITCHENS_IN_DB = (int) kitchenRepository.count();

    }
}
