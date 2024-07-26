package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {
    private final UserRepository userRepository;



    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "FilterService::findUserByFullName",
            key = "#fullName"
    )
    public List<User> findUserByFullName(String fullName) {
        return userRepository.findUserByFullname(fullName);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "FilterService::getAllUsers"
    )
    public List<User> getAllUsers() {
         List<User> users = userRepository.findAll();
    for (User user : users) {
    Hibernate.initialize(user.getPhone());
    }
    return users;

    }


}
