package com.zhenya.ru.bank.service;

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
import com.zhenya.ru.bank.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SecurityServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEmailRepository userEmailRepository;

    @Mock
    private UserPhoneRepository userPhoneRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SecurityServiceImpl securityService;

    private User user;
    private UserEmail userEmail;
    private UserPhones userPhones;
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private BigDecimal balance;
    private String password;

    @BeforeEach
    public void setUp() {
        username = "testuser";
        fullname = "Test User";
        email = "testuser@example.com";
        phone = "1234567890";
        dateOfBirth = LocalDate.of(1990, 1, 1);
        balance = BigDecimal.valueOf(1000);
        password = "password";
        user = User.builder()
                .username(username)
                .fullname(fullname)
                .dateOfBirth(dateOfBirth)
                .balance(balance)
                .password(password)
                .role(Role.USER)
                .build();
        userEmail = UserEmail.builder()
                .user(user)
                .email(email)
                .build();
        userPhones = UserPhones.builder()
                .user(user)
                .phone(phone)
                .build();
    }

    @Test
    public void testRegisterSuccess() {
        when(userRepository.existsUserByUsername(username)).thenReturn(false);
        when(userEmailRepository.existsUserEmailByEmail(email)).thenReturn(false);
        when(userPhoneRepository.existsUserPhonesByPhone(phone)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn(password);

        ResponseEntity<?> response = securityService.register(username, fullname, email, phone, dateOfBirth, balance, password);

        assertEquals(ResponseEntity.ok("Регистрация прошла успешно"), response);
    }

    @Test
    public void testRegisterUsernameExists() {
        when(userRepository.existsUserByUsername(username)).thenReturn(true);

        ResponseEntity<?> response = securityService.register(username, fullname, email, phone, dateOfBirth, balance, password);

        assertEquals(ResponseEntity.badRequest().body("Пользователь с таким именем уже существует"), response);
    }

    @Test
    public void testRegisterEmailExists() {
        when(userRepository.existsUserByUsername(username)).thenReturn(false);
        when(userEmailRepository.existsUserEmailByEmail(email)).thenReturn(true);

        ResponseEntity<?> response = securityService.register(username, fullname, email, phone, dateOfBirth, balance, password);

        assertEquals(ResponseEntity.badRequest().body("Пользователь с таким email уже существует"), response);
    }

    @Test
    public void testRegisterPhoneExists() {
        when(userRepository.existsUserByUsername(username)).thenReturn(false);
        when(userEmailRepository.existsUserEmailByEmail(email)).thenReturn(false);
        when(userPhoneRepository.existsUserPhonesByPhone(phone)).thenReturn(true);

        ResponseEntity<?> response = securityService.register(username, fullname, email, phone, dateOfBirth, balance, password);

        assertEquals(ResponseEntity.badRequest().body("Пользователь с таким номером телефона уже существует"), response);
    }

    @Test
    public void testAuthorizeSuccess() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtil.generateToken(authentication)).thenReturn("token");

        ResponseEntity<TokenDTO> response = securityService.authorize(username, password);

        assertNotNull(response.getBody());
        assertEquals("token", response.getBody().jwt());
    }

    @Test
    public void testAuthorizeInvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Неправильный пароль или имя"));

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            securityService.authorize(username, password);
        });

        assertEquals("Неправильный пароль или имя", exception.getMessage());
    }

    @Test
    public void testAuthorizeInternalError() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<TokenDTO> response = securityService.authorize(username, password);

        assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), response);
    }
}
