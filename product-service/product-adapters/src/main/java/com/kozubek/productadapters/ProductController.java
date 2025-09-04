package com.kozubek.productadapters;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@GetMapping
	public String hello() {
		return "Hello World from PaymentController";
	}

	@PostMapping
	public String test(@RequestBody TestClass testClass) {
		return "Hello World from PaymentController " + testClass;
	}
}
