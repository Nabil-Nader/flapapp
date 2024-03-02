package com.flap.app.service;

import com.flap.app.dto.*;
import com.flap.app.model.Product;
import com.flap.app.model.Role;
import com.flap.app.model.User;
import com.flap.app.repository.ProductRepository;
import com.flap.app.repository.UserRepository;
import com.flap.app.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpleTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private UserServiceImple userService;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    private User user;

    private Product product;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setRole(Role.Buyer);
        user.setDeposit(BigDecimal.valueOf(1000));

        product = new Product();
        product.setId(1L);
        product.setProductName("Test Product");
        product.setCost(BigDecimal.valueOf(50));
        product.setAmountAvailable(10);


    }



    @Test
    void testCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("testPassword");
        userDto.setRole(Role.Buyer.toString());

        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.createUser(userDto);

        assertEquals("User has been saved", result);
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("updatedUser");
        userDto.setPassword("updatedPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.updateUser(1L, userDto);

        assertEquals("User has been updated", result);
        assertEquals(userDto.getUsername(), user.getUsername());
        assertEquals(userDto.getPassword(), user.getPassword());
    }

    @Test
    void testDepositMoney() {
        BuyerInformationDto dto = new BuyerInformationDto();
        dto.setDeposit(BigDecimal.valueOf(100));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        BuyerInformationDto result = userService.depositMoney(dto, 1L);

        assertEquals(user.getDeposit(), BigDecimal.valueOf(1100));
        assertEquals(user.getId(), result.getId());
    }

    @Test
    void testBuyProducts() {
        PurchaseRequest request = new PurchaseRequest();
        request.setProductId(1L);
        request.setAmount(5);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        PurchaseResponse result = userService.buyProducts(request, 1L);

        assertEquals(BigDecimal.valueOf(250), result.totalSpent());
        assertEquals(5, product.getAmountAvailable());
        assertEquals(BigDecimal.valueOf(750), user.getDeposit());
    }

}