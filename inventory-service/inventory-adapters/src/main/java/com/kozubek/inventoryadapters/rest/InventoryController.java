package com.kozubek.inventoryadapters.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @GetMapping
    public String hello() {
        return "Hello World from InventoryController";
    }
}
