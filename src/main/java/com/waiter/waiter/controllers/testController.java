package com.waiter.waiter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController {
    @GetMapping("/try-form")
    public String getAllActors(){
        return "aa";
    }
}
