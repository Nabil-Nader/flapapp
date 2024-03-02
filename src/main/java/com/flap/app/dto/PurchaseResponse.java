package com.flap.app.dto;

import com.flap.app.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


public record PurchaseResponse (

        BigDecimal totalSpent,
        Product productPurchased,
        BigDecimal change
){


}
