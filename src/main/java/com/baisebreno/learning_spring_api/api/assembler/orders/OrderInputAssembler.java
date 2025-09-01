package com.baisebreno.learning_spring_api.api.assembler.orders;

import com.baisebreno.learning_spring_api.api.assembler.GenericAssembler;
import com.baisebreno.learning_spring_api.api.model.input.AddressInputModel;
import com.baisebreno.learning_spring_api.api.model.input.ItemOrderInput;
import com.baisebreno.learning_spring_api.api.model.input.OrderInput;
import com.baisebreno.learning_spring_api.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderInputAssembler {
    @Autowired
    private GenericAssembler assembler;

    public Order toDomainObject(OrderInput orderInput){
        return assembler.toSubject(orderInput, Order.class);
    }

    public void copyToDomainObject(OrderInput orderInput, Order order){
        assembler.copy(orderInput,order);
    }

    public Order toDomain(OrderInput in) {
        Order order = new Order();

        // restaurant
        Restaurant r = new Restaurant();
        r.setId(in.getRestaurant().getId());
        order.setRestaurant(r);

        // delivery address
        AddressInputModel ai = in.getDeliveryAddress();
        Address addr = new Address();
        addr.setPostCode(ai.getPostCode());
        addr.setStreet(ai.getStreet());
        addr.setNumber(ai.getNumber());
        addr.setCounty(ai.getCounty());

        City c = new City();
        c.setId(ai.getCity().getId());
        addr.setCity(c);

        order.setDeliveryAddress(addr);

        // payment
        PaymentMethod pm = new PaymentMethod();
        pm.setId(in.getPaymentType().getId());
        order.setPaymentMethod(pm);

        // items
        List<OrderItem> items = new ArrayList<>();
        for (ItemOrderInput it : in.getItems()) {
            OrderItem oi = new OrderItem();
            Product p = new Product(); p.setId(it.getProductId());
            oi.setProduct(p);
            oi.setQuantity(it.getQuantity());
            oi.setNotes(it.getNotes());
            oi.setOrder(order); // keep back-ref consistent
            items.add(oi);
        }
        order.setItems(items);

        return order;
    }
}
