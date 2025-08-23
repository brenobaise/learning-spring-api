package com.baisebreno.learning_spring_api.core.jackson;

import com.baisebreno.learning_spring_api.api.model.mixin.CityMixin;
import com.baisebreno.learning_spring_api.api.model.mixin.KitchenMixin;
import com.baisebreno.learning_spring_api.domain.model.City;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

@Component
public class JacksonMixinModule extends SimpleModule {

    public JacksonMixinModule(){
        setMixInAnnotation(City.class, CityMixin.class);
        setMixInAnnotation(Kitchen.class, KitchenMixin.class);
    }
}
