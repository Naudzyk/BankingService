package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.dto.TokenDTO;
import com.zhenya.ru.bank.exception.InvalidCredentialsException;
import com.zhenya.ru.bank.exception.NotValidArgumentException;
import com.zhenya.ru.bank.models.Role;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserEmail;
import com.zhenya.ru.bank.models.UserPhones;
import com.zhenya.ru.bank.repository.UserEmailRepository;
import com.zhenya.ru.bank.repository.UserPhoneRepository;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.security.JwtUtil;
import com.zhenya.ru.bank.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserEmailRepository userEmailRepository;
    private final UserPhoneRepository userPhoneRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User register(String username, String fullname, String email, String phone, LocalDate dateOfBirth, BigDecimal balance, String password) {
        if (email == null || phone == null ||
                email.isEmpty() || phone.isEmpty()) {
            throw new NotValidArgumentException("Пароль или логин или email или username или fullname или date или balance или password не могут быть пустыми или состоять только из пробелов.");
        }
        if (password.length() < 5 || password.length() > 30) {
            throw new NotValidArgumentException("Длина пароля должна составлять от 5 до 30 символов.");
        }

            if (userRepository.existsUserByUsername(username)) {
                throw new SecurityException("Пользователь с таким именем ужу есть уже есть");
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setFullname(fullname);
            newUser.setDateOfBirth(dateOfBirth);
            newUser.setBalance(balance);
            newUser.setInitialBalance(balance);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRole(Role.USER);

            User savedUser = userRepository.save(newUser);



               UserEmail newuserEmail = new UserEmail();
               newuserEmail.setUser(savedUser);
               newuserEmail.setEmail(email);
               userEmailRepository.save(newuserEmail);


                UserPhones newuserPhone = new UserPhones();
                newuserPhone.setUser(savedUser);
                newuserPhone.setPhone(phone);
                userPhoneRepository.save(newuserPhone);



            return savedUser;
    }

    @Override
    public TokenDTO authorize(String username, String password) {
    Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Неправильный пароль или имя");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);
        return new TokenDTO(jwt);
    }
}
