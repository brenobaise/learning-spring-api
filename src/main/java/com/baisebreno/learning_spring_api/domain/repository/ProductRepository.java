package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.Product;
import com.baisebreno.learning_spring_api.domain.model.ProductPhoto;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, ProductRepositoryQueries{

    @Query("from Product where restaurant.id = :restaurant and id = :product")
    Optional<Product> findById(@Param("restaurant") Long restaurantId, @Param("product")Long productId);

    List<Product> findAllByRestaurant(Restaurant restaurant);

    @Query("from Product p where p.active = true and p.restaurant = :restaurant ")
    List<Product> findActiveByRestaurant(Restaurant restaurant);

    /*
    Fetch the ProductPhoto that belongs to the product with ID = :productId,
     but only if that product also belongs to the restaurant with ID = :restaurantId.”
     */
    @Query("select pp from ProductPhoto pp join pp.product p "
            + "where p.restaurant.id = :restaurantId and pp.product.id = :productId")
    Optional<ProductPhoto> findPhotoById(Long restaurantId, Long productId);
}
