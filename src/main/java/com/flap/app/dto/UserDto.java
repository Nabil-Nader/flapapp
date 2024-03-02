package com.flap.app.dto;

import com.flap.app.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @NotBlank(message = "username can not be null or empty")
    private String username;

    @NotBlank(message = "password can not be null or empty")
    private String password;
    @NotBlank(message = "Role can not be null or empty")

    @NotNull(message = "role can not be null")
    private String role;

}
