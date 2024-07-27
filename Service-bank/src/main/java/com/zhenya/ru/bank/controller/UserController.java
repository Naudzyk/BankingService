package com.zhenya.ru.bank.controller;


import com.zhenya.ru.bank.dto.TransferDTO;
import com.zhenya.ru.bank.dto.UserEmailDTO;
import com.zhenya.ru.bank.dto.UserPhoneDTO;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.security.SecurityUtils;
import com.zhenya.ru.bank.service.MoneyTransferService;
import com.zhenya.ru.bank.service.UserEmailService;
import com.zhenya.ru.bank.service.UserPhoneService;
import com.zhenya.ru.bank.service.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserEmailService userEmailService;
    private final UserPhoneService userPhoneService;
    private final UserRepository userRepository;
    private final MoneyTransferService moneyTransferService;


    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferDTO transferDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();

        if (SecurityUtils.isValidLogin(username)) {
            throw new SecurityException("Некорректный username!");
        }

        if (transferDTO.money() == null || username.isBlank() || transferDTO.getUsername().isBlank()) {
           ResponseEntity.badRequest().body(" GetUsername или Money не могут быть пустыми или состоять только из пробелов.");
        }

        return moneyTransferService.transfer(username, transferDTO.getUsername(), transferDTO.money());

    }

    @PostMapping("/deletePhone")
    public ResponseEntity<?> deletePhone(@RequestBody UserPhoneDTO userPhoneDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Long id = userDetails.getId();

        if (SecurityUtils.isValidLogin(username)) {
            throw new SecurityException("Некорректный username!");
        }


        if (userPhoneDTO.phone().isBlank()) {
            ResponseEntity.badRequest().body("Phone не может быть пустым или состоять только из пробелов.");
        }

        return userPhoneService.deletePhone(userPhoneDTO.phone(),id);
    }

    @PostMapping("/deleteEmail")
    public ResponseEntity<?> deleteEmail(@RequestBody UserEmailDTO userEmailDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Long id = userDetails.getId();

        if (SecurityUtils.isValidLogin(username)) {
            throw new SecurityException("Некорректный username!");
        }

        if (userEmailDTO.email().isBlank()) {
            ResponseEntity.badRequest().body("Email не может быть пустым или состоять только из пробелов.");
        }

       return userEmailService.deleteEmail(userEmailDTO.email(),id);
    }

  @GetMapping("/getPhone")
    public ResponseEntity<?> getPhone(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (SecurityUtils.isValidLogin(userDetails.getUsername())) throw new SecurityException("Некорректный username!");
        User user = (User) userRepository.findAllById(userDetails.getId());
        List<String> userPhones = userPhoneService.getPhone(user);
        return ResponseEntity.ok("Телефоны : " + userPhones);


    }

    @GetMapping("/getEmail")
    public ResponseEntity<?>getEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (SecurityUtils.isValidLogin(userDetails.getUsername())){ throw new SecurityException("Некорректный username!");}
        User user = (User) userRepository.findAllById(userDetails.getId());
        List<String> userEmails = userEmailService.getEmails(user);
        return ResponseEntity.ok("Emails : " + userEmails);

    }

    @PostMapping("/submitEmail")
    public ResponseEntity<?> submitEmail(@RequestBody UserEmailDTO userEmailDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();

        if (!userRepository.existsUserByUsername(username)) {
            throw new IllegalArgumentException("Пользователь с таким именем не найден");
        }

        if (SecurityUtils.isValidLogin(username)) {
            throw new SecurityException("Некорректный username!");
        }

        if (userEmailDTO.email().isBlank()) {
            ResponseEntity.badRequest().body("Email не может быть пустым или состоять только из пробелов.");
        }

        return userEmailService.save(userEmailDTO.email(), username);
    }

    @PostMapping("/submitPhone")
    public ResponseEntity<?> submitPhone(@RequestBody UserPhoneDTO userPhoneDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();

        if (!userRepository.existsUserByUsername(username)) {
            throw new IllegalArgumentException("Пользователь с таким именем не найден");
        }

        if (SecurityUtils.isValidLogin(username)) {
            throw new SecurityException("Некорректный username!");
        }

        if (userPhoneDTO.phone().isBlank()) {
            ResponseEntity.badRequest().body("Phone не может быть пустым или состоять только из пробелов.");
        }

        return userPhoneService.save(userPhoneDTO.phone(), username);
    }

    @GetMapping ("/getBalance")
    public ResponseEntity<?> getBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (SecurityUtils.isValidLogin(userDetails.getUsername())) {
            throw new SecurityException("Некорректный username!");
        }
        User user = userRepository.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok("Balance : " + user.getBalance());
    }



}





