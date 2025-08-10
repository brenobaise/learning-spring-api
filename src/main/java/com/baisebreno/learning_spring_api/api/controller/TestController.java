package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private KitchenRepository kitchenRepository;

//    @GetMapping("/kitchens/by-name")
//    public List<Kitchen> kitchensByName(@RequestParam String name){
//        return kitchenRepository.(name);
//    }
}
