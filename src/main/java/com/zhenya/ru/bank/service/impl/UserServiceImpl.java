package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public List<User> showAllUsers() {
        return userRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "UserService::getUserById",
            key = "#id"
    )
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Не найден пользователь по ID"));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "UserService::getUserByUsername",
            key = "#username"
    )
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));

    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }


}
