package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> showAllUsers();

    User getUserById(Integer id);

    Optional<User> getUserByUsername(String username);

    User save(User user);






}
