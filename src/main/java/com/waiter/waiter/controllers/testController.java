package com.waiter.waiter.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class testController<DefaultOidcUser> {
    @GetMapping("/test")
    public String getAllActors() {
        return "aa";
    }

    // @RequestMapping("/resource")
    //public Map<String, Object> home(Authentication authentication) {
        /*Map<String , Object> userDetails = ((DefaultOidcUser)authentication.getPrincipal()).getAttributes();
        System.out.println(userDetails.get("name"));
        System.out.println(userDetails.get("email"));
        System.out.println(userDetails.get("given_name"));*/
    @GetMapping("/hello")
    public String hello(@CurrentSecurityContext(expression="authentication?.name")
                        String username) {
        return "Hello, " + username + "!";
    }

}
