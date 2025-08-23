package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.KitchenNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.model.PaymentMethod;
import com.baisebreno.learning_spring_api.domain.repository.PaymentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService {
    @Autowired
    PaymentTypeRepository repository;

    public List<PaymentMethod> getAll(){
        return repository.findAll();
    }

    public PaymentMethod findOne(Long id){
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Payment Method with id: %d not found", id)));
    }

    public PaymentMethod save(PaymentMethod method){
        return repository.save(method);
    }

    public void delete(Long id) {
        PaymentMethod foundMethod = findOne(id);

        repository.deleteById(id);
    }
}
