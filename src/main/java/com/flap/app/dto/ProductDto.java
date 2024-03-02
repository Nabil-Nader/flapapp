package com.flap.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {

    private int amountAvailable;

    private BigDecimal cost;

    private String productName;

    private Long id;


}
