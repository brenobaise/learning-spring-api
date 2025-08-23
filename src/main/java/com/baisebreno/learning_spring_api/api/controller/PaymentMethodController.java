package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.payment.PaymentMethodModelAssembler;
import com.baisebreno.learning_spring_api.api.model.PaymentMethodModel;
import com.baisebreno.learning_spring_api.api.model.input.PaymentMethodInputModel;
import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.RestaurantNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.PaymentMethod;
import com.baisebreno.learning_spring_api.domain.repository.PaymentTypeRepository;
import com.baisebreno.learning_spring_api.domain.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentMethodController {
    @Autowired
    PaymentTypeRepository paymentTypeRepository;

    @Autowired
    PaymentMethodService paymentMethodService;

    @Autowired
    PaymentMethodModelAssembler assembler;

    @GetMapping()
    public List<PaymentMethodModel> getAll(){
        return assembler.toCollectionModel(paymentTypeRepository.findAll());
    }
    @GetMapping("/{id}")
    public PaymentMethodModel find(@PathVariable Long id){
        return assembler.toModel(paymentMethodService.findOne(id));

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentMethodModel create(@Valid @RequestBody PaymentMethodInputModel paymentMethodInputModel){
        try{
            PaymentMethod paymentMethod = assembler.disassemble(paymentMethodInputModel);
            return assembler.toModel(paymentMethodService.save(paymentMethod));
        }catch (EntityNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public PaymentMethodModel update(@PathVariable Long id,
                                     @Valid @RequestBody PaymentMethodInputModel paymentMethodInputModel){
        PaymentMethod currentMethod = paymentMethodService.findOne(id);

        assembler.copyToDomainObject(paymentMethodInputModel, currentMethod);

        currentMethod = paymentMethodService.save(currentMethod);


        return assembler.toModel(currentMethod);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        paymentMethodService.delete(id);
    }
}
