package com.baisebreno.learning_spring_api.infrastructure.service;

import com.baisebreno.learning_spring_api.domain.filter.DailySalesFilter;
import com.baisebreno.learning_spring_api.domain.model.Order;
import com.baisebreno.learning_spring_api.domain.model.OrderStatus;
import com.baisebreno.learning_spring_api.domain.model.dto.DailySales;
import com.baisebreno.learning_spring_api.domain.service.SalesQueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class SalesQueryServiceImpl implements SalesQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DailySales> findDailySales(DailySalesFilter filter) {

        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(DailySales.class);
        var root = query.from(Order.class);

        var predicates = new ArrayList<Predicate>();

        var functionCreatedDate = builder.function("date", LocalDate.class,root.get("createdDate"));

         var selection  = builder.construct(DailySales.class,
                 functionCreatedDate,
                 builder.count(root.get("id")),
                 builder.sum(root.get("total")));

         if(filter.getRestaurantId() != null){
             predicates.add(builder.equal(root.get("restaurant"), filter.getRestaurantId()));
         }

        if(filter.getInitialCreatedDate() != null){
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"),
                    filter.getInitialCreatedDate()));
        }

        if(filter.getEndingCreatedDate() != null){
            predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"),
                    filter.getEndingCreatedDate()));
        }

        predicates.add(root.get("status").in(
                OrderStatus.CONFIRMED, OrderStatus.DELIVERED));


         query.select(selection);
         query.where(predicates.toArray(new Predicate[0]));
         query.groupBy(functionCreatedDate);


        return entityManager.createQuery(query).getResultList();
    }
}
