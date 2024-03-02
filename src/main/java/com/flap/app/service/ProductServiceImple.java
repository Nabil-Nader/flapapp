package com.flap.app.service;

import com.flap.app.dto.ProductDto;
import com.flap.app.exception.ProductNotFound;
import com.flap.app.model.Product;
import com.flap.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImple implements ProductService{

    private final ModelMapper modelMapper ;

    private final ProductRepository productRepository ;


    @Override
    public ProductDto createProduct(ProductDto dto,Long userId) {
    Product product = modelMapper.map(dto,Product.class);
    product.setSellerId(userId);
    productRepository.save(product);
    return modelMapper.map(product,ProductDto.class);
    }

    @Override
    public List<ProductDto> getProducts() {
        List<Product>productList = productRepository.findAll();

        return productList.stream()
                .map(product -> modelMapper.map(product, ProductDto.class)).toList();
        }



    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Optional<Product>optional = productRepository.findById(id);
        if(optional.isEmpty()){
            throw new ProductNotFound("Product does not exists with this id" + id);
        }else{
            Product product = optional.get();

            productRepository.save(product);
            return modelMapper.map(product,ProductDto.class);
        }    }
}
