package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserPhones;
import com.zhenya.ru.bank.repository.UserPhoneRepository;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.impl.UserPhoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserPhoneServiceTest {
    @Mock
    private UserPhoneRepository userPhoneRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserPhoneServiceImpl userPhoneService;

    private User user;
    private UserPhones userPhone;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testUser")
                .fullname("Test User")
                .build();

         userPhone = UserPhones.builder()
                .user(user)
                .phone("1234567890")
                .build();

    }

@Test
void testSavePhoneAlreadyExists() {
    when(userPhoneRepository.existsUserPhonesByPhone(userPhone.getPhone())).thenReturn(true);



    ResponseEntity<?> response = userPhoneService.save(userPhone.getPhone(), user.getUsername());

    assertEquals(ResponseEntity.badRequest().body("Ошибка такой номер возможно зарегестрирован"), response);

    verify(userPhoneRepository, never()).save(any(UserPhones.class));
}


    @Test
    void testSaveNewPhone() {
        when(userPhoneRepository.existsUserPhonesByPhone(userPhone.getPhone())).thenReturn(false);
        when(userRepository.getUserByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<?> response = userPhoneService.save(userPhone.getPhone(), user.getUsername());

        assertEquals(ResponseEntity.ok("Добавлен phone " + userPhone.getPhone()), response);
        verify(userPhoneRepository, times(1)).save(any(UserPhones.class));
    }

    @Test
    public void testDeletePhone_Success() {
        String phone = "1234567890";
        Long userId = 1L;

        when(userPhoneRepository.findUserIdByPhone(phone)).thenReturn(userId);

        when(userPhoneRepository.deleteByPhone(phone)).thenReturn(1);

        ResponseEntity<?> response = userPhoneService.deletePhone(phone, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Телефон удален :" + phone, response.getBody());
    }

    @Test
    public void testDeletePhone_InvalidPhone() {
        String phone = "1234567890";
        Long userId = 1L;

        when(userPhoneRepository.findUserIdByPhone(phone)).thenReturn(userId);

        when(userPhoneRepository.deleteByPhone(phone)).thenReturn(0);

        ResponseEntity<?> response = userPhoneService.deletePhone(phone, userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Некоректный телефон", response.getBody());
    }

    @Test
    public void testDeletePhone_WrongUserId() {
        String phone = "1234567890";
        Long userId = 1L;
        Long wrongUserId = 2L;

        when(userPhoneRepository.findUserIdByPhone(phone)).thenReturn(userId);

        ResponseEntity<?> response = userPhoneService.deletePhone(phone, wrongUserId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Некоректный телефон", response.getBody());
    }

    @Test
    void testGetPhone() {
        List<UserPhones> userPhonesList = new ArrayList<>();
        userPhonesList.add(userPhone);

        when(userPhoneRepository.findUserPhonesByUser(user)).thenReturn(userPhonesList);

        List<String> phones = userPhoneService.getPhone(user);

        assertEquals(1, phones.size());
        assertEquals(userPhone.getPhone(), phones.get(0));
        verify(userPhoneRepository, times(1)).findUserPhonesByUser(user);
    }
}
