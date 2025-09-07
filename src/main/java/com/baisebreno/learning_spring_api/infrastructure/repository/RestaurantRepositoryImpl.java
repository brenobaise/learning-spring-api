package com.baisebreno.learning_spring_api.infrastructure.repository;


import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is just an example of how to tell Spring Data Jpa to implement custom queries.
 * We annotate the class with the @Repository, inject a PersistenceContext, and we add the implementation of the method here.
 * In order to make the link between this implementation and the interface declaring the methods,
 * we have to extend the interface at the RestaurantRepository,
 * just like we usually do for the JpaRepository
 *
 * In other words, we created our own {@link org.springframework.data.jpa.repository.JpaRepository}
 */
@Repository
public class RestaurantRepositoryImpl implements RestaurantRepositoryQueries {
    @PersistenceContext
    EntityManager entityManager;

    /**
     * This is a dynamic search using CriteriaAPI.
     * It creates an arrayList of Predicates, which are used on a where clause inside a criteria query.

     */
    @Override
    public List<Restaurant> find(String name, BigDecimal initialDeliveryRate, BigDecimal finalDeliveryRate) {

        /*
         * This is the equivalent SQL:
         * select
         *     r.id as id,
         *     r.name as name,
         *     r.delivery_rate as deliveryRate,
         *     r.kitchen_id as kitchenId
         * from
         *     restaurant r
         */

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> criteria = builder.createQuery(Restaurant.class);



        Root<Restaurant> root = criteria.from(Restaurant.class); // >> "from Restaurant" in JPQL

        var predicates = new ArrayList<Predicate>();

        if(StringUtils.hasText(name)){
            predicates.add(builder.like(root.get("name"),"%"+ name + "%"));
        }

        if(initialDeliveryRate != null){
            predicates.add(builder.greaterThanOrEqualTo(root.get("deliveryRate"), initialDeliveryRate));
        }

        if(finalDeliveryRate != null){
            predicates.add(builder.lessThanOrEqualTo(root.get("deliveryRate"), finalDeliveryRate));
        }


        criteria.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Restaurant> query =  entityManager.createQuery(criteria);
                return query.getResultList();
    }

//    public List<Restaurant> find(String name, BigDecimal initialDeliveryRate, BigDecimal finalDeliveryRate ){
//
//        var jpql = new StringBuilder();
//        jpql.append("from Restaurant where 0 = 0 ");
//
//        var parameters = new HashMap<String, Object>();
//
//        if(StringUtils.hasLength(name)){ // if it's not empty and its > than 0 characters
//            jpql.append("and name like :name ");
//            parameters.put("name", "%" + name + "%");
//        }
//        if(initialDeliveryRate != null){
//            jpql.append("and deliveryRate >= :initialDeliveryRate ");
//            parameters.put("initialDeliveryRate", initialDeliveryRate);
//        }
//        if(finalDeliveryRate != null){
//            jpql.append("and deliveryRate <= :finalDeliveryRate ");
//            parameters.put("finalDeliveryRate", finalDeliveryRate);
//        }
//
//        System.out.println(jpql.toString());
//        TypedQuery<Restaurant> query = entityManager.createQuery(jpql.toString(), Restaurant.class);
//
//        parameters.forEach(query::setParameter);
//
//        return query.getResultList();
//    }


}
