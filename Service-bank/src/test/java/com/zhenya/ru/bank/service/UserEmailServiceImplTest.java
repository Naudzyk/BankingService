package com.zhenya.ru.bank.service;


import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserEmail;
import com.zhenya.ru.bank.repository.UserEmailRepository;
import com.zhenya.ru.bank.repository.UserRepository;

import com.zhenya.ru.bank.service.impl.UserEmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEmailServiceImplTest {

    @Mock
    private UserEmailRepository userEmailRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserEmailServiceImpl userEmailService;

    private User user;
    private UserEmail userEmail;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .username("testUser")
                .fullname("Test User")
                .build();

        userEmail = UserEmail.builder()
                .id(1L)
                .user(user)
                .email("test@example.com")
                .build();

    }

@Test
public void testSaveEmailSuccess() {
    when(userEmailRepository.existsUserEmailByEmail(userEmail.getEmail())).thenReturn(false);

    when(userRepository.getUserByUsername(user.getUsername())).thenReturn(user);

    when(userEmailRepository.save(any(UserEmail.class))).thenReturn(userEmail);

    ResponseEntity<?> response = userEmailService.save(userEmail.getEmail(), user.getUsername());

    assertEquals(ResponseEntity.ok("Добавлен email " + userEmail.getEmail()), response);
}

@Test
public void testSaveEmailAlreadyExists() {
        when(userEmailRepository.existsUserEmailByEmail(userEmail.getEmail())).thenReturn(true);


        ResponseEntity<?> response = userEmailService.save(userEmail.getEmail(), user.getUsername());

        assertEquals(ResponseEntity.badRequest().body("Ошибка такой email уже зарегестрирован"), response);
        verify(userEmailRepository, never()).save(any(UserEmail.class));
}



    @Test
    public void testGetEmails() {
        when(userEmailRepository.findUserEmailByUser(user)).thenReturn(Collections.singletonList(userEmail));

        List<String> emails = userEmailService.getEmails(user);

        assertEquals(1, emails.size());
        assertEquals(userEmail.getEmail(), emails.get(0));
    }

    @Test
    public void testDeleteEmail_Success() {
        String email = "test@example.com";
        Long userId = 1L;

        when(userEmailRepository.findUserIdByEmail(email)).thenReturn(userId);

        when(userEmailRepository.deleteByEmail(email)).thenReturn(1L);

        ResponseEntity<?> response = userEmailService.deleteEmail(email, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email удален: " + email, response.getBody());
    }

    @Test
    public void testDeleteEmail_InvalidEmail() {
        String email = "test@example.com";
        Long userId = 1L;

        when(userEmailRepository.findUserIdByEmail(email)).thenReturn(userId);

        when(userEmailRepository.deleteByEmail(email)).thenReturn(0L);

        ResponseEntity<?> response = userEmailService.deleteEmail(email, userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Некоректный email", response.getBody());
    }

    @Test
    public void testDeleteEmail_WrongUserId() {
        String email = "test@example.com";
        Long userId = 1L;
        Long wrongUserId = 2L;

        when(userEmailRepository.findUserIdByEmail(email)).thenReturn(userId);

        ResponseEntity<?> response = userEmailService.deleteEmail(email, wrongUserId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Некоректный email", response.getBody());
    }

}


