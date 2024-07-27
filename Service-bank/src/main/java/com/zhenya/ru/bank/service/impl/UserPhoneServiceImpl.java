package com.zhenya.ru.bank.service.impl;


import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserPhones;
import com.zhenya.ru.bank.repository.UserPhoneRepository;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.UserPhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPhoneServiceImpl implements UserPhoneService {
    private final UserPhoneRepository userPhoneRepository;
    private final UserRepository userRepository;


    @Override
    public ResponseEntity<?> save(String phone, String username) {
        if(!userPhoneRepository.existsUserPhonesByPhone(phone)) {
            User user = userRepository.getUserByUsername(username);
            UserPhones newuserPhone = UserPhones.builder()
                    .user(user)
                    .phone(phone)
                    .build();

            userPhoneRepository.save(newuserPhone);
            return ResponseEntity.ok("Добавлен phone " + phone);
        }
        return ResponseEntity.badRequest().body("Ошибка такой номер возможно зарегестрирован");
    }

    @Override
    public ResponseEntity<?> deletePhone(String phone,Long id) {

        if (userPhoneRepository.findUserIdByPhone(phone) == id) {
            if (userPhoneRepository.deleteByPhone(phone) == 1) {
                return ResponseEntity.ok("Телефон удален :" + phone);
                    }else{
                return ResponseEntity.badRequest().body("Некоректный телефон");
            }
        } else {
            return ResponseEntity.badRequest().body("Некоректный телефон");
        }
    }




    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "UserPhoneService::getPhone",
            key = "#user"
    )
    public List<String> getPhone(User user) {
        List<String> phone = new ArrayList<>();
        List<UserPhones> userPhones = userPhoneRepository.findUserPhonesByUser(user);

        for(UserPhones userPhones1 : userPhones){
            phone.add(userPhones1.getPhone());
        }
        return phone;
    }

}
