package com.zhenya.ru.bank.util;

import com.zhenya.ru.bank.models.Role;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserEmail;
import com.zhenya.ru.bank.models.UserPhones;
import com.zhenya.ru.bank.repository.UserEmailRepository;
import com.zhenya.ru.bank.repository.UserPhoneRepository;
import com.zhenya.ru.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
@Component
public class UserUtils {
    @Autowired
    private static UserRepository userRepository;
    @Autowired
    private static UserEmailRepository userEmailRepository;
    @Autowired
    private static UserPhoneRepository userPhoneRepository;

    public static User getNewUser(){
        LocalDate date = LocalDate.of(2000, 1 ,1);
        User newUser = new User();
            newUser.setUsername("username");
            newUser.setFullname("fullname");
            newUser.setDateOfBirth(date);
            newUser.setBalance(BigDecimal.valueOf(600));
            newUser.setInitialBalance(BigDecimal.valueOf(600));
            newUser.setPassword("password");
            newUser.setRole(Role.USER);

            User savedUser = userRepository.save(newUser);

            UserEmail newuserEmail = new UserEmail();
               newuserEmail.setUser(savedUser);
               newuserEmail.setEmail("email@mail.ru");
               userEmailRepository.save(newuserEmail);

           UserPhones newuserPhone = new UserPhones();
                newuserPhone.setUser(savedUser);
                newuserPhone.setPhone("0000000");
                userPhoneRepository.save(newuserPhone);

                User username = userRepository.getUserByUsername(newUser.getUsername());
        return newUser;
    }
}
