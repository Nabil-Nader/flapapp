package com.flap.app.dto;

import com.flap.app.model.Role;

public record UserResponse (Long id , String token, Role role,String username){
}
