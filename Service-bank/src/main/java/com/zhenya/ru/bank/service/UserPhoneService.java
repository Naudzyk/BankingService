package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserPhoneService {
    ResponseEntity<?> save(String phone, String username);

    ResponseEntity<?> deletePhone(String phone,Long id);

    List<String> getPhone (User user);


}
