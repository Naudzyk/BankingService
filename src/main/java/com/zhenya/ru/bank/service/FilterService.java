package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.models.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FilterService {

    List<User> findByBirthDateGreaterThan(LocalDate birthdate);


    List<User> findUserByFullName( String fullName);

    List<User> getAllUsers();


}
