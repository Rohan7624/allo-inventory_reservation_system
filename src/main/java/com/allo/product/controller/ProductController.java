package com.allo.product.controller;

import com.allo.product.dto.ProductResponse;
import com.allo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getAllProducts();
    }
}