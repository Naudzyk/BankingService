package com.zhenya.ru.bank.controller;

import com.zhenya.ru.bank.dto.TransferDTO;
import com.zhenya.ru.bank.dto.UserEmailDTO;
import com.zhenya.ru.bank.dto.UserPhoneDTO;
import com.zhenya.ru.bank.exception.NotValidArgumentException;
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
    public ResponseEntity<?> transfer(@RequestBody TransferDTO transferDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (SecurityUtils.isValidLogin(userDetails.getUsername())) throw new SecurityException("Некорректный username!");
        if (transferDTO.money() == null ||
        userDetails.getUsername() == null || userDetails.getUsername().isBlank() || userDetails.getUsername().isEmpty()
        || transferDTO.getUsername() == null || transferDTO.getUsername().isBlank() || transferDTO.getUsername().isEmpty()){
            throw new NotValidArgumentException("Username ,GetUsername,Money не могут быть пустыми или состоять только из пробелов.");
        }

        moneyTransferService.transfer(userDetails.getUsername(),transferDTO.getUsername(),transferDTO.money());
        return ResponseEntity.ok("Первевел "  + transferDTO.getUsername() + " количество " + transferDTO.money());

    }


    @PostMapping  ("/deletePhone")
    public ResponseEntity<?> deletePhone(@RequestBody UserPhoneDTO userPhoneDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (SecurityUtils.isValidLogin(userDetails.getUsername())) throw new SecurityException("Некорректный username!");
        if (userPhoneDTO.phone().isEmpty() || userPhoneDTO.phone() == null || userPhoneDTO.phone().isBlank()){
            throw new NotValidArgumentException("Phone не могут быть пустыми или состоять только из пробелов.");
        }
        userPhoneService.deletePhone(userPhoneDTO.phone());
        return ResponseEntity.ok("Phone удален:" + userPhoneDTO.phone());

    }


    @PostMapping("/deleteEmail")
    ResponseEntity<?> deletePhone(@RequestBody String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (SecurityUtils.isValidLogin(userDetails.getUsername())) throw new SecurityException("Incorrect username!");
        if (email.isEmpty() || email == null ){
            throw new NotValidArgumentException("Email не могут быть пустыми или состоять только из пробелов.");
        }
        userEmailService.deleteEmail(email);
        return ResponseEntity.ok("Email удален:" + email);


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
        if (!userRepository.existsUserByUsername(userDetails.getUsername())) {
        throw new IllegalArgumentException("Пользователь с таким именем не найден");
    }
    if (SecurityUtils.isValidLogin(userDetails.getUsername())){ throw new SecurityException("Некорректный username!");}

    String userEmail =  userEmailService.save(userEmailDTO.email(), userDetails.getUsername());

    if (userEmailDTO.email().isEmpty() || userEmailDTO.email() == null){
            throw new NotValidArgumentException("Email не могут быть пустыми или состоять только из пробелов.");
        }

    return ResponseEntity.ok("Добавился email : " + userEmail);
    }




    @PostMapping("/submitPhone")
    public ResponseEntity<?> submitPhone(@RequestBody UserPhoneDTO userPhoneDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            if (!userRepository.existsUserByUsername(userDetails.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем не найден");
        }
        if (SecurityUtils.isValidLogin(userDetails.getUsername())){ throw new SecurityException("Некорректный username!");}
            String userPhones = userPhoneService.save(userPhoneDTO.phone(), userDetails.getUsername());

        if (userPhones == null) {
            throw new IllegalStateException("Не удалось сохранить phone пользователя");
        }
         if (userPhoneDTO.phone().isEmpty() || userPhoneDTO.phone() == null || userPhoneDTO.phone().isBlank()){
            throw new NotValidArgumentException("Email не могут быть пустыми или состоять только из пробелов.");
        }

        return ResponseEntity.ok("Добавился телефон : " + userPhones);
        }

    @GetMapping ("/getBalance")
    public ResponseEntity<?> getBalance(){
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                if (SecurityUtils.isValidLogin(userDetails.getUsername())){ throw new SecurityException("Некорректный username!");}
                User user = userRepository.getUserByUsername(userDetails.getUsername());
                return ResponseEntity.ok("Balance : " + user.getBalance());
        }
    }





