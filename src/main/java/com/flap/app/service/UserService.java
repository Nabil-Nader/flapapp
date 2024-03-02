package com.flap.app.service;

import com.flap.app.dto.*;

public interface UserService {
    String createUser(UserDto userDto);

    UserDto getUserById(Long id);

    void deleteUser(Long id);

    String updateUser(Long id, UserDto user);

    BuyerInformationDto depositMoney(BuyerInformationDto dto,Long userId);

    PurchaseResponse buyProducts(PurchaseRequest request,Long userId);

    UserResponse login(UserLoginRequest userLoginRequest);
}
