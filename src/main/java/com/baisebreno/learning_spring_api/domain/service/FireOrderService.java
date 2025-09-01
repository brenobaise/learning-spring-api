package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.OrderNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.*;
import com.baisebreno.learning_spring_api.domain.repository.OrderRepository;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FireOrderService {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RestaurantRegistryService restaurantRegistryService;

    @Autowired
    CityRegistryService cityRegistryService;

    @Autowired
    UserRegistryService userRegistryService;

    @Autowired
    ProductRegistryService productRegistryService;

    @Autowired
    PaymentMethodService paymentMethodService;


    @Transactional
    public Order fire(Order order){

//        System.out.println(order.toString());

        validateOrder(order);
        validateItems(order);

        order.setDeliveryRate(order.getRestaurant().getDeliveryRate());
        order.calculateTotal();
        return orderRepository.save(order);
    }

    private void validateOrder(Order order){
        City city = cityRegistryService.findOne(order.getDeliveryAddress().getCity().getId());
        User user = userRegistryService.findOne(order.getUser().getId());
        Restaurant restaurant = restaurantRegistryService.findOne(order.getRestaurant().getId());
        PaymentMethod paymentMethod = paymentMethodService.findOne(order.getPaymentMethod().getId());

        order.getDeliveryAddress().setCity(city);
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setPaymentMethod(paymentMethod);

        if(restaurant.doesNotAcceptPaymentType(paymentMethod)){
            throw new BusinessException(String.format("Payment type %s is not accepted by this establishment"
                    , paymentMethod.getDescription()));
        }
    }

    private void validateItems(Order order){
        order.getItems().forEach(orderItem -> {
            Product product = productRegistryService.findOne(
                    order.getRestaurant().getId(), orderItem.getProduct().getId()
            );

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setUnitPrice(product.getPrice());
        });
    }

    public Order findOne(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow( () -> new OrderNotFoundException(orderId));

    }
}
