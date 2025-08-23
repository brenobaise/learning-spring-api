package com.baisebreno.learning_spring_api.api.assembler.payment;

import com.baisebreno.learning_spring_api.api.assembler.GenericAssembler;
import com.baisebreno.learning_spring_api.api.model.PaymentMethodModel;
import com.baisebreno.learning_spring_api.api.model.input.PaymentMethodInputModel;
import com.baisebreno.learning_spring_api.domain.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMethodModelAssembler {

    @Autowired
    private GenericAssembler genericAssembler;


    public PaymentMethodModel toModel(PaymentMethod paymentMethod){
        return genericAssembler.toSubject(paymentMethod, PaymentMethodModel.class);
    }

    public List<PaymentMethodModel> toCollectionModel(List<PaymentMethod> methods){
        return genericAssembler.toCollectionModel(methods,PaymentMethodModel.class);
    }

    public PaymentMethod disassemble(PaymentMethodInputModel paymentMethodInputModel){
        return genericAssembler.toSubject(paymentMethodInputModel, PaymentMethod.class);
    }

}
