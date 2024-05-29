package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.exception.NotValidArgumentException;
import com.zhenya.ru.bank.exception.SecurityException;
import com.zhenya.ru.bank.exception.UserNotFoundException;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserPhones;
import com.zhenya.ru.bank.repository.UserPhoneRepository;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.UserPhoneService;
import com.zhenya.ru.bank.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPhoneServiceImpl implements UserPhoneService {
    private final UserPhoneRepository userPhoneRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    @Override
    public String save(String phone, String username) {
        if(userPhoneRepository.existsUserPhonesByPhone(phone)){
            throw new SecurityException("Такой phone уже зарегестрирован");
        }
        User user = userRepository.getUserByUsername(username);
         if (user == null) {
        throw new IllegalArgumentException("Пользователь с таким именем не найден");
    }

        UserPhones userPhones = new UserPhones();
        userPhones.setUser(user);
        userPhones.setPhone(phone);

        userPhoneRepository.save(userPhones);
        return phone;
    }

    @Override
    public void deletePhone( String phone) {
       userPhoneRepository.deleteByPhone(phone);


    }



    @Override
    public List<String> getPhone(User user) {
        List<String> phone = new ArrayList<>();
        List<UserPhones> userPhones = userPhoneRepository.findUserPhonesByUser(user);

        for(UserPhones userPhones1 : userPhones){
            phone.add(userPhones1.getPhone());
        }
        return phone;
    }

     private Integer getIdByUsername(String username){
        Optional<User> userOptional = userService.getUserByUsername(username);
        if(userOptional.isPresent()) {
            return userOptional.get().getId();
        } else{
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
