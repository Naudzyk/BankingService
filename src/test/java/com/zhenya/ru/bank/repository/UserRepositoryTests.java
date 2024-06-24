package com.zhenya.ru.bank.repository;

import com.zhenya.ru.bank.models.Role;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserEmail;
import com.zhenya.ru.bank.models.UserPhones;
import com.zhenya.ru.bank.util.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    private UserEmail userEmail;

    private UserPhones userPhones;
    @Autowired
    private UserEmailRepository userEmailRepository;
    @Autowired
    private UserPhoneRepository userPhoneRepository;

    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save user")
    public void givenUserObject_whenSave_thenUserIsCreated(){
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
            assertThat(savedUser).isNotNull();
            assertThat(savedUser.getId()).isNotNull();

               UserEmail newuserEmail = new UserEmail();
               newuserEmail.setUser(savedUser);
               newuserEmail.setEmail("email@mail.ru");
               userEmailRepository.save(newuserEmail);
            assertThat(newuserEmail).isNotNull();

                UserPhones newuserPhone = new UserPhones();
                newuserPhone.setUser(savedUser);
                newuserPhone.setPhone("0000000");
                userPhoneRepository.save(newuserPhone);
            assertThat(newuserPhone).isNotNull();
    }
    @Test
    @DisplayName("Test get User by Username")
    public void gerUserByUsername(){
        LocalDate date = LocalDate.of(2000, 1 ,1);
        User newUser = UserUtils.getNewUser();

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

                assertThat(username).isNotNull();
                assertThat(username).isEqualTo(newUser);

    }

}
