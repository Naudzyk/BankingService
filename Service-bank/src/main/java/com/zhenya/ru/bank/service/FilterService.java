package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.models.User;


import java.util.List;

public interface FilterService {


    List<User> findUserByFullName( String fullName);

    List<User> getAllUsers();


}
