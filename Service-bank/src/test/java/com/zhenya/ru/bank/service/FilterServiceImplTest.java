package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.impl.FilterServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilterServiceImplTest {
   @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FilterServiceImpl filterService;

    @Test
    public void testFindUserByFullName() {
        String fullName = "John Doe";
        User user1 = new User();
        user1.setUsername("User1");
        user1.setFullname(fullName);

        User user3 = new User();
        user3.setUsername("User3");
        user3.setFullname(fullName);

       List<User> users = Arrays.asList(user1, user3);

    when(userRepository.findUserByFullname(fullName)).thenReturn(users);

    List<User> result1 = filterService.findUserByFullName(fullName);

    assertEquals(2, result1.size());
    assertTrue(result1.contains(user1));
    assertTrue(result1.contains(user3));

    verify(userRepository, times(1)).findUserByFullname(fullName);
    }
}
