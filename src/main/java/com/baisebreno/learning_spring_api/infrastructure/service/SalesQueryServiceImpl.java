package com.baisebreno.learning_spring_api.infrastructure.service;

import com.baisebreno.learning_spring_api.domain.filter.DailySalesFilter;
import com.baisebreno.learning_spring_api.domain.model.Order;
import com.baisebreno.learning_spring_api.domain.model.dto.DailySales;
import com.baisebreno.learning_spring_api.domain.service.SalesQueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public class SalesQueryServiceImpl implements SalesQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DailySales> findDailySales(DailySalesFilter dailySalesFilter) {

        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(DailySales.class);
        var root = query.from(Order.class);

        var functionCreatedDate = builder.function("date", LocalDate.class,root.get("createdDate"));

         var selection  = builder.construct(DailySales.class,
                 functionCreatedDate,
                 builder.count(root.get("id")),
                 builder.sum(root.get("total")));

         query.select(selection);
         query.groupBy(functionCreatedDate);


        return entityManager.createQuery(query).getResultList();
    }
}
