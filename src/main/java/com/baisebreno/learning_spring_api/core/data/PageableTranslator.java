package com.baisebreno.learning_spring_api.core.data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PageableTranslator {

    public static Pageable translate(Pageable pageable, Map<String,String> fieldMappings){
        var orders = pageable.getSort().stream()
                // if the property doesn't exist, ignore it
                .filter(order -> fieldMappings.containsKey(order.getProperty()))
                .map(order -> new Sort.Order(
                        order.getDirection(),
                        fieldMappings.get(order.getProperty())))
                .toList();

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
    }
}
