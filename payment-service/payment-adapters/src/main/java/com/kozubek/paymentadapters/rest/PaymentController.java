package com.kozubek.paymentadapters.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @GetMapping
    public String hello() {
        return "Hello World from PaymentController";
    }

    @PostMapping
    public String test(@RequestBody TestClass testClass) {
        return "Hello World from PaymentController " + testClass;
    }
}
