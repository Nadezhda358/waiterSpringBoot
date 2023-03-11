package com.waiter.waiter.helpingClasses;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.waiter.waiter.entities.Drink;
import com.waiter.waiter.repositories.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class DrinkConverter implements Converter <String, Optional<Drink>>{
    @Autowired
    DrinkRepository drinkRepository;
    @Override
    public Optional<Drink> convert(String id){

    System.out.println("Converting dish with id="+id);
    int parseId=Integer.parseInt(id);
    return drinkRepository.findById(parseId);
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
