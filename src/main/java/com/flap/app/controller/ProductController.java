package com.flap.app.controller;

import com.flap.app.dto.ProductDto;
import com.flap.app.dto.UserDto;
import com.flap.app.service.ProductService;
import com.flap.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping
    public List<ProductDto> getProducts() {
        return productService.getProducts();
    }

    @PreAuthorize("hasAnyRole('seller')")
    @PostMapping()
    public ProductDto createUser(@Valid @RequestBody ProductDto dto,@RequestAttribute Long userId) {
        return productService.createProduct(dto,userId);
    }

    @PreAuthorize("hasAnyRole('seller')")
    @PutMapping("/{id}")
    public ProductDto updateUser(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        productService.deleteProductById(id);
    }




}
