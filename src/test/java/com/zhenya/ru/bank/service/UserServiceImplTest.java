package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testShowAllUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setFullname("John Doe");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setFullname("LOx Pidr");
        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.showAllUsers();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));

        verify(userRepository, times(1)).findAll();
    }
    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User();
        user.setUsername("user1");
        user.setFullname("John Doe");
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of((user)));

        User result = userService.getUserById(userId);

        assertEquals(user, result);

        verify(userRepository, times(1)).findById(userId);
    }
    @Test
    public void testGetUserByUsername() {
        String username = "user1";
        User user = new User();
        user.setUsername("user1");
        user.setFullname("John Doe");

        when(userRepository.findByUsername(username)).thenReturn(user);

        Optional<User> result = userService.getUserByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetUserByUsername_NotFound() {
        String username = "nonExistingUser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        Optional<User> result = userService.getUserByUsername(username);

        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findByUsername(username);
    }
    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUsername("user1");
        user.setFullname("John Doe");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertEquals(user, result);

        verify(userRepository, times(1)).save(user);
    }
}
