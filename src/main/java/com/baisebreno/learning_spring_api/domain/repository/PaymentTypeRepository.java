package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.PaymentType;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PaymentTypeRepository {
    List<PaymentType> getAll();
    PaymentType getById(Long id);
    PaymentType save(PaymentType paymentType);
    void remove(PaymentType paymentType);
}
