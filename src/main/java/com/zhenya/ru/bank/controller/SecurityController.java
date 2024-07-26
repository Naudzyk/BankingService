package com.zhenya.ru.bank.controller;

import com.zhenya.ru.bank.dto.AuthDTO;
import com.zhenya.ru.bank.dto.TokenDTO;
import com.zhenya.ru.bank.dto.UserDTO;
import com.zhenya.ru.bank.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;


    @PostMapping("/singin")
    ResponseEntity<TokenDTO> singin(@RequestBody AuthDTO authDTO){
        return securityService.authorize(authDTO.username(),authDTO.password());

    }

    @PostMapping("/singup")
    ResponseEntity<?> singup(@RequestBody UserDTO userDTO){
        return securityService.register(userDTO.username(), userDTO.fullname(), userDTO.email(), userDTO.phone(),userDTO.dateOfBirth(),userDTO.balance(), userDTO.password());



    }
}
