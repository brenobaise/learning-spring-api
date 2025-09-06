package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.orders.OrderInputAssembler;
import com.baisebreno.learning_spring_api.api.assembler.orders.OrderModelAssembler;
import com.baisebreno.learning_spring_api.api.assembler.orders.OrderSummaryModelAssembler;
import com.baisebreno.learning_spring_api.api.model.OrderModel;
import com.baisebreno.learning_spring_api.api.model.OrderSummaryModel;
import com.baisebreno.learning_spring_api.api.model.input.OrderInput;
import com.baisebreno.learning_spring_api.core.data.PageableTranslator;
import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Order;
import com.baisebreno.learning_spring_api.domain.model.User;
import com.baisebreno.learning_spring_api.domain.repository.OrderRepository;
import com.baisebreno.learning_spring_api.domain.repository.filter.OrderFilter;
import com.baisebreno.learning_spring_api.domain.service.FireOrderService;
import com.baisebreno.learning_spring_api.domain.service.OrderRegistryService;
import com.baisebreno.learning_spring_api.infrastructure.repository.spec.OrderSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    OrderRegistryService orderRegistryService;

    @Autowired
    OrderSummaryModelAssembler summaryModelAssembler;
    @Autowired
    OrderModelAssembler orderModelAssembler;

    @Autowired
    OrderInputAssembler orderInputAssembler;

    @Autowired
    FireOrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping
    public Page<OrderSummaryModel> search(OrderFilter orderFilter,@PageableDefault(size = 5) Pageable pageable){
        pageable = translatePageable(pageable);

        Page<Order> orderPage = orderRepository.findAll(OrderSpecs.withFilter(orderFilter), pageable);
        List<OrderSummaryModel> summaryModels = summaryModelAssembler.toCollectionModel(orderPage.getContent());

        return new PageImpl<>(summaryModels, pageable, orderPage.getTotalElements());
    }

    @GetMapping("/{orderCode}")
    public OrderModel find(@PathVariable String orderCode){
        return orderModelAssembler.toModel(orderRegistryService.findOne(orderCode));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrderModel newOrder(@Valid @RequestBody OrderInput orderInput){
        try{
            Order newOrder = orderInputAssembler.toDomain(orderInput);

            newOrder.setUser(new User());
            newOrder.getUser().setId(1L);

            newOrder = orderService.fire(newOrder);

            return orderModelAssembler.toModel(newOrder);
        }catch (EntityNotFoundException e){
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{orderId}")
    public void updateOrder(){}

    @DeleteMapping("/{orderId}")
    public void deleteOrder(){}

    private Pageable translatePageable(Pageable apiPageable ){
        var mapping = Map.of(
                "orderCode", "code",
                "subtotal", "subtotal",
                "deliveryRate", "deliveryRate",
                "total", "total",
                "createdDate", "createdDate",
                "restaurant.name", "restaurant.name",
                "restaurant.id", "restaurant.id",
                "user.id", "user.id",
                "user.name", "user.name"
        );

        return PageableTranslator.translate(apiPageable, mapping);

    }

}
