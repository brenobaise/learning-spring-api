package com.baisebreno.learning_spring_api;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;

import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.service.KitchenRegistryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class KitchenRegistryServiceTestsIT {

    @Autowired
    KitchenRegistryService kitchenRegistryService;

    @Autowired
    RestaurantRegistryService restaurantRegistryService;


	@Test
	void contextLoads() {

	}

    @Test
    void testKitchenRegistryServiceWithSuccess(){

        //Arrange
        Kitchen kitchen = new Kitchen();
        kitchen.setName("Chai");

        // Act
        kitchen = kitchenRegistryService.save(kitchen);

        // Assert
        assertThat(kitchen).isNotNull();
        assertThat(kitchen.getId()).isNotNull();
    }

    @Test()
    void testKitchenRegistryServiceWithNoName(){
        //Arrange
        Kitchen kitchen = new Kitchen();
        kitchen.setName(null);

        // Act
        ConstraintViolationException expectedErr =
                Assertions.assertThrows(ConstraintViolationException.class, () ->{
                    kitchenRegistryService.save(kitchen);
                });

        // Assert
        assertThat(expectedErr).isNotNull();
    }

    @Test
    public void shouldFailWhenRemovingKitchenInUse(){
        // Arrange
        Kitchen kitchen = new Kitchen();
        kitchen.setName("AKsDPOASKd");
        kitchenRegistryService.save(kitchen);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("adada");
        restaurant.setDeliveryRate(BigDecimal.valueOf(20));
        restaurant.setKitchen(kitchen);
        restaurantRegistryService.save(restaurant);

        // Act
        EntityInUseException expectedErr =
                Assertions.assertThrows(EntityInUseException.class, () -> {
                    kitchenRegistryService.remove(kitchen.getId());
                });
        // Assert
        assertThat(expectedErr).isNotNull();

    }

    @Test
    public void shouldFailWhenRemovingNonExistentKitchen(){
        // Arrange
        Long unknownId = 10L;
        // Act

        EntityNotFoundException expectedErr =
                Assertions.assertThrows(EntityNotFoundException.class, () -> {
                    kitchenRegistryService.remove(unknownId);
                });

        // Assert
        assertThat(expectedErr).isNotNull();
    }
}
