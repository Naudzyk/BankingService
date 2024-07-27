package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.dto.TokenDTO;
import com.zhenya.ru.bank.exception.InvalidCredentialsException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    public ResponseEntity<?> register(String username, String fullname, String email, String phone, LocalDate dateOfBirth, BigDecimal balance, String password) {
        try {
            validateRegistrationInputs(username, email, phone, password);


            if (userRepository.existsUserByUsername(username)) {
                return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
            }
            if (userEmailRepository.existsUserEmailByEmail(email)) {
                return ResponseEntity.badRequest().body("Пользователь с таким email уже существует");
            }
            if (userPhoneRepository.existsUserPhonesByPhone(phone)) {
                return ResponseEntity.badRequest().body("Пользователь с таким номером телефона уже существует");
            }


            User newUser = User.builder()
                    .username(username)
                    .fullname(fullname)
                    .dateOfBirth(dateOfBirth)
                    .balance(balance)
                    .password(passwordEncoder.encode(password))
                    .role(Role.USER)
                    .build();

            User savedUser = userRepository.save(newUser);


            UserEmail newuserEmail = UserEmail.builder()
                    .user(savedUser)
                    .email(email)
                    .build();
            userEmailRepository.save(newuserEmail);


            UserPhones newuserPhone = UserPhones.builder()
                    .user(savedUser)
                    .phone(phone)
                    .build();
            userPhoneRepository.save(newuserPhone);


            return ResponseEntity.ok("Регистрация прошла успешно");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при регистрации");
        }
    }

@Override
    public ResponseEntity<TokenDTO> authorize(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(authentication);
            TokenDTO tokenDTO = new TokenDTO(jwt);
            return ResponseEntity.ok(tokenDTO);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Неправильный пароль или имя");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    private void validateRegistrationInputs(String username, String email, String phone, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Номер телефона не может быть пустым");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

    }
}
