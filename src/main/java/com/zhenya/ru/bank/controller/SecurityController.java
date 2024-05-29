package com.zhenya.ru.bank.controller;

import com.zhenya.ru.bank.dto.AuthDTO;
import com.zhenya.ru.bank.dto.TokenDTO;
import com.zhenya.ru.bank.dto.UserDTO;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.secutity.JwtUtil;
import com.zhenya.ru.bank.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;


    @PostMapping("/singin")
    ResponseEntity<?> singin(@RequestBody AuthDTO authDTO){
        return ResponseEntity.ok(securityService.authorize(authDTO.username(),authDTO.password()));

    }

    @PostMapping("/singup")
    ResponseEntity<?> singup(@RequestBody UserDTO userDTO){
        securityService.register(userDTO.username(), userDTO.fullname(), userDTO.email(), userDTO.phone(),userDTO.dateOfBirth(),userDTO.balance(), userDTO.password());
         return ResponseEntity.ok("Пользователь создан : " + userDTO.username());


    }
}
