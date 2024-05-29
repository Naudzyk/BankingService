package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserPhones;

import java.util.List;

public interface UserPhoneService {
    String save(String phone,String username);

    void deletePhone(String phone);

    List<String> getPhone (User user);


}
