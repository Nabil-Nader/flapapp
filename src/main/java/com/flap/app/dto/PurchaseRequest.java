package com.flap.app.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PurchaseRequest {


    private Long productId;

    private int amount;

}
