package com.flap.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "users_app",
        indexes = {@Index(name = "idx_username", columnList = "username", unique = true)})

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String username;

    private String password;

    private BigDecimal deposit = BigDecimal.ZERO;

    // this will work as our default user
    @Enumerated(EnumType.STRING)
    private Role role =  Role.Buyer;



}
