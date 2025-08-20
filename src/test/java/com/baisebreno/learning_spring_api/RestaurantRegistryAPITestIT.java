package com.baisebreno.learning_spring_api;

import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepository;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import com.baisebreno.learning_spring_api.util.DatabaseCleaner;
import com.baisebreno.learning_spring_api.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestPropertySource("/application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantRegistryAPITestIT {


    private String validRestaurantJson;
    private String noDeliveryRateRestaurantJson;
    private String noKitchenRestaurantJson;
    private String unexistentKitchenRestaurantJson;

    private Restaurant seededRestaurant; // we’ll use its id for GET-by-id

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleaner databaseCleaner;

    @Autowired
    RestaurantRegistryService restaurantRegistryService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    KitchenRepository kitchenRepository;

    @BeforeEach
    void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/restaurants";

        // Load request payloads (fail fast if the resource is missing)
        validRestaurantJson = ResourceUtils.getContentFromResource("/json/restaurant-newyork-bbq.json");
        noDeliveryRateRestaurantJson = ResourceUtils.getContentFromResource("/json/restaurant-newyork-bbq-no-deliveryRate.json");
        noKitchenRestaurantJson = ResourceUtils.getContentFromResource("/json/restaurant-newyork-bbq-no-kitchen.json");
        unexistentKitchenRestaurantJson = ResourceUtils.getContentFromResource("/json/restaurant-newyork-notfound-kitchen.json");

        databaseCleaner.clearTables();
        prepareData();
    }

    @Test
    void shouldReturn200_WhenListRestaurants() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldReturn201_WhenCreateRestaurant() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(validRestaurantJson)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void shouldReturn400_WhenCreateRestaurantWithoutDeliveryRate() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(noDeliveryRateRestaurantJson)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturn400_WhenCreateRestaurantWithoutKitchen() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(noKitchenRestaurantJson)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturn404_WhenCreateRestaurantWithNonexistentKitchen() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(unexistentKitchenRestaurantJson)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnCorrectResponse_WhenGetExistingRestaurantById() {
        given()
                .pathParam("restaurantId", seededRestaurant.getId())
                .accept(ContentType.JSON)
                .when()
                .get("/{restaurantId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                // If Restaurant.name is NOT annotated with @JsonProperty("name"),
                // the property is "name" in JSON:
                .body("name", equalTo(seededRestaurant.getName()));
        // If you WANT "name", annotate Restaurant.name with @JsonProperty("name") and switch to:
        // .body("name", equalTo(seededRestaurant.getName()));
    }

    @Test
    void shouldReturn404_WhenGetNonexistentRestaurantById() {
        long nonexistentId = 1000L;
        given()
                .pathParam("restaurantId", nonexistentId)
                .accept(ContentType.JSON)
                .when()
                .get("/{restaurantId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void prepareData() {
        Kitchen k1 = new Kitchen();
        k1.setName("Brasileira");
        kitchenRepository.save(k1);

        Kitchen k2 = new Kitchen();
        k2.setName("Americana");
        kitchenRepository.save(k2);

        Restaurant r1 = new Restaurant();
        r1.setName("Brasileira Top");
        r1.setDeliveryRate(new BigDecimal("10.00"));
        r1.setKitchen(k1);
        seededRestaurant = restaurantRepository.save(r1);

        Restaurant r2 = new Restaurant();
        r2.setName("Americana Food");
        r2.setDeliveryRate(new BigDecimal("10.00"));
        r2.setKitchen(k2);
        restaurantRepository.save(r2);
    }
}
