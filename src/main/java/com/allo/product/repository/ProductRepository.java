package com.allo.product.repository;

import com.allo.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Locale;
import java.util.UUID;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
}