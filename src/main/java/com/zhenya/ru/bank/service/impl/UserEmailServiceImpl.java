package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.exception.SecurityException;
import com.zhenya.ru.bank.exception.UserNotFoundException;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserEmail;
import com.zhenya.ru.bank.repository.UserEmailRepository;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.UserEmailService;
import com.zhenya.ru.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserEmailServiceImpl implements UserEmailService {
    private final UserEmailRepository userEmailRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    @Override
    public String save(String email, String username) {


        if (userEmailRepository.existsUserEmailByEmail(email)) {
            throw new SecurityException("Такой email уже есть: " + email);
        }

        User user = userRepository.getUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("Пользователь с таким именем не найден: " + username);
        }

        UserEmail userEmail = new UserEmail();
        userEmail.setEmail(email);
        userEmail.setUser(user);

        userEmailRepository.save(userEmail);
        return email;
    }



    @Override
    public List<String> getEmails(User user) {
     List<String> emails = new ArrayList<>();
    List<UserEmail> userEmails = userEmailRepository.findUserEmailByUser(user);

    for (UserEmail userEmail : userEmails) {
        emails.add(userEmail.getEmail());
    }

    return emails;
}


    @Override
    public void deleteEmail(String email) {
        userEmailRepository.deleteByEmail(email);

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
