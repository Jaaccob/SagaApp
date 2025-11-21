package com.kozubek.productadapters.rest;

import com.kozubek.ddd.annotation.architecture.portsandadapters.DrivingAdapter;
import com.kozubek.productadapters.rest.dto.CreateProductRequest;
import com.kozubek.productadapters.rest.dto.CreateProductResponse;
import com.kozubek.productadapters.rest.dto.GetDetailsProductResponse;
import com.kozubek.productapplication.ProductApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@DrivingAdapter
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductCommandRestMapper productCommandRestMapper;
	private final ProductApplicationService productApplicationService;
	private final ProductQueryRestMapper productQueryRestMapper;

	@GetMapping("/hello")
	public String hello() {
		return "Hello World from ProductController";
	}

	@PostMapping
	public ResponseEntity<CreateProductResponse> createProduct(@RequestBody final CreateProductRequest createProductRequest) {
		return ResponseEntity.ok(new CreateProductResponse(productApplicationService.createProduct(productCommandRestMapper.createProductRequestToCreateProductCommand(createProductRequest)).id()));
	}

	@GetMapping("/{productId}")
	public ResponseEntity<GetDetailsProductResponse> getDetailsProduct(@PathVariable("productId") final UUID productId) {
		return ResponseEntity.ok(productQueryRestMapper.productToGetDetailsProductResponse(productApplicationService.getProduct(productId)));
	}
}
