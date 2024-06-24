package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {
    private final UserRepository userRepository;


    @Override
    public List<User> findByBirthDateGreaterThan(LocalDate birthdate) {
        return userRepository.findByDateOfBirthGreaterThan(birthdate);
    }



    @Override
    public List<User> findUserByFullName(String fullName) {
        return userRepository.findUserByFullname(fullName);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
