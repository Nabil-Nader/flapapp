package com.flap.app.service;

import com.flap.app.dto.ProductDto;
import com.flap.app.model.Product;
import com.flap.app.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImpleTest {

    @Mock
    private ProductRepository productRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper() ;

    @InjectMocks
    private ProductServiceImple productService;

    @BeforeEach
    public void setUp() {
        // Set up mock responses
    }

    @Test
     void testCreateProduct() {
        // Prepare test data
        ProductDto dto = new ProductDto();
        Long userId = 1L;

        // Test service method
        productService.createProduct(dto, userId);

        // Verify that the repository's save method was called
        verify(productRepository, times(1)).save(any());
    }

    @Test
     void testGetProducts() {
        // Prepare test data
        Product product = new Product();
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductDto> productDtos = productService.getProducts();

        // Verify that the repository's findAll method was called
        verify(productRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(), any());
        assertEquals(1, productDtos.size());
    }

    @Test
     void testDeleteProductById() {
        // Prepare test data
        Long id = 1L;

        // Test service method
        productService.deleteProductById(id);

        // Verify that the repository's deleteById method was called
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
     void testUpdateProduct() {
        // Prepare test data
        Long id = 1L;
        ProductDto dto = new ProductDto();
        Optional<Product> optionalProduct = Optional.of(new Product());
        when(productRepository.findById(id)).thenReturn(optionalProduct);

        // Test service method
        productService.updateProduct(id, dto);

        // Verify that the repository's findById method was called
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(any());
        verify(modelMapper, times(1)).map(any(), any());
    }
}