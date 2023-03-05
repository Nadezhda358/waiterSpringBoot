package com.waiter.waiter.helpingClasses;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DishConverter implements Converter<String, Optional<Dish>> {
    @Autowired
    DishRepository dishRepository;
    @Override
    public Optional<Dish> convert(String id){
         System.out.println("Trying to convert id="+id+" into a dish.");
        int parsedId=Integer.parseInt(id);
         return dishRepository.findById(parsedId);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }


}
