package com.flap.app.service;

import com.flap.app.dto.*;
import com.flap.app.exception.BalanceNotEnough;
import com.flap.app.exception.ProductNotFound;
import com.flap.app.exception.UserNotFound;
import com.flap.app.model.Product;
import com.flap.app.model.Role;
import com.flap.app.model.User;
import com.flap.app.repository.ProductRepository;
import com.flap.app.repository.UserRepository;
import com.flap.app.security.JwtTokenUtil;
import com.flap.app.util.HashingUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImple implements UserService{

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    public String createUser(UserDto userDto) {
        User user = modelMapper.map(userDto,User.class);
        if(userDto.getRole().equalsIgnoreCase(Role.Buyer.toString())|| userDto.getRole().equalsIgnoreCase(Role.Seller.toString())){
            user.setRole(userDto.getRole().equalsIgnoreCase(Role.Buyer.toString())?Role.Buyer : Role.Seller);
            user.setPassword(HashingUtil.sha256(userDto.getPassword()));
        }else {
            throw new UserNotFound("invalid role");
        }
        userRepository.save(user);
        return "User has been saved";
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if(optional.isEmpty()){
            throw new UserNotFound("User does not exists with this id"+id);
        }else{
            return modelMapper.map(optional.get(),UserDto.class);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public String updateUser(Long id, UserDto dto) {
        Optional<User> optional = userRepository.findById(id);
        if(optional.isEmpty()) {
            throw new UserNotFound("User does not exists with this id" + id);
        }else{
            User user = optional.get();
            user.setUsername(dto.getUsername());
            user.setPassword(dto.getPassword());
            userRepository.save(user);
            return "User has been updated";

        }

    }

    /*
        find user validete user ,
        validate money
        add money to user ,
        send queure 
     */

    @Override
    public BuyerInformationDto depositMoney(BuyerInformationDto dto,Long userId) {
        Optional<User> optional = userRepository.findById(userId);
        if(optional.isEmpty()) {
            throw new UserNotFound("User does not exists with this id" + dto.getId());
        }else{
            User user = optional.get();
            user.setDeposit(user.getDeposit().add(dto.getDeposit()));
            userRepository.save(user);
            return modelMapper.map(user,BuyerInformationDto.class);

        }
    }

    @Override
    public PurchaseResponse buyProducts(PurchaseRequest request,Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFound("User not found"));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ProductNotFound("Product not found"));

        BigDecimal totalCost = product.getCost().multiply(BigDecimal.valueOf(request.getAmount()));

        if(user.getDeposit().doubleValue() < totalCost.doubleValue()){
            throw new BalanceNotEnough("Insufficient funds");

        }

        if (product.getAmountAvailable() < request.getAmount()) {
            throw new BalanceNotEnough("Not enough products available");
        }

        BigDecimal change = user.getDeposit().subtract(totalCost);

        user.setDeposit(change);
        userRepository.save(user);
        int remainingProducts = product.getAmountAvailable() - request.getAmount();
        product.setAmountAvailable(remainingProducts);
        productRepository.save(product);


        return new PurchaseResponse(totalCost, product, change);


    }

    @Override
    public UserResponse login(UserLoginRequest userLoginRequest) {
     User user = userRepository.findByUsername(userLoginRequest.username()).orElseThrow(() -> new RuntimeException("User not found"));

     if(!user.getPassword().equalsIgnoreCase(HashingUtil.sha256(userLoginRequest.password()))) {
         throw new UserNotFound("password not correct");
     }
     String token = getAuthenticationToken(user.getId().toString(),user.getUsername(),user);
        return new UserResponse(user.getId(),token,user.getRole(),user.getUsername());
    }

    private String getAuthenticationToken(String id, String displayName, User user) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(id, displayName)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenUtil.generateToken(user);

    }
}
