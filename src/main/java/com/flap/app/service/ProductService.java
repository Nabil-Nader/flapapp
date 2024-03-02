package com.flap.app.service;

import com.flap.app.dto.ProductDto;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto dto,Long userId);

    List<ProductDto> getProducts();

    void deleteProductById(Long id);

    ProductDto updateProduct(Long id, ProductDto dto);



}
