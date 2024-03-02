package com.flap.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BuyerInformationDto {

    private String username;
    private BigDecimal deposit;

    private Long id;


}
