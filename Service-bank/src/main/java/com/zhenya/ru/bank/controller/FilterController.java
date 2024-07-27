package com.zhenya.ru.bank.controller;

import com.zhenya.ru.bank.exception.NotValidArgumentException;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.service.FilterService;
import com.zhenya.ru.bank.service.UserService;
import com.zhenya.ru.bank.service.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {
    private final FilterService filterService;


    @GetMapping ("/fullname")
    public List<User> getUserByFullname () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (filterService.findUserByFullName(userDetails.getFullname()) != null) {
            return filterService.findUserByFullName(userDetails.getFullname());
        } else {
            throw new NotValidArgumentException("Ошибка");

        }
    }

    @GetMapping("/all")
    public List<User> getAllUser(){
        return filterService.getAllUsers();
    }

}
