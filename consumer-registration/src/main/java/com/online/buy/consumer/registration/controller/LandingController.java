package com.online.buy.consumer.registration.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/landing")
public class LandingController {

    @RequestMapping("/home")
    public String home() {
        return "Welcome to the consumer registration portal!";
    }
}
