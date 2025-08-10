package com.baisebreno.learning_spring_api.infrastructure.repository;

import com.baisebreno.learning_spring_api.domain.model.PaymentType;
import com.baisebreno.learning_spring_api.domain.repository.PaymentTypeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class PaymentTypeRepositoryImpl implements PaymentTypeRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<PaymentType> getAll() {
        return entityManager.createQuery("from PaymentType ", PaymentType.class).getResultList();
    }

    @Override
    public PaymentType getById(Long id) {
        return entityManager.find(PaymentType.class, id);
    }

    @Transactional
    @Override
    public PaymentType save(PaymentType paymentType) {
        return entityManager.merge(paymentType);
    }

    @Transactional
    @Override
    public void remove(PaymentType paymentType) {
        paymentType = getById(paymentType.getId());
        entityManager.remove(paymentType);
    }
}
