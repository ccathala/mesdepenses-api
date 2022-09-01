package com.ccathala.mesdepensesapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {

    @Value("${app.profile.name}")
    private String appProfileName;
    
    @GetMapping(value = "/hello")
    @ResponseStatus(HttpStatus.CREATED)
    public String hello() {
        return "Hello " + appProfileName;
    }
}
