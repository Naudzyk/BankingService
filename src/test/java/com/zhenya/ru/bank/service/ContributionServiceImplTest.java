package com.zhenya.ru.bank.service;


import com.zhenya.ru.bank.models.Contribution;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.ContributionRepository;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.impl.ContributionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContributionServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContributionRepository contributionRepository;

    @InjectMocks
    private ContributionServiceImpl contributionService;

    private User user;
    private Contribution contribution;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setBalance(BigDecimal.valueOf(1000));

        contribution = new Contribution();
        contribution.setBalance(BigDecimal.valueOf(500));
        contribution.setUser(user);
    }

    @Test
    void testMoneyToTheAccount_noContribution() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(contributionRepository.findByUser(user)).thenReturn(null);

        ResponseEntity<String> response = contributionService.moneyToTheAccount("testuser");
        assertEquals("У вас нету вклада", response.getBody());
    }

    @Test
    void testMoneyToTheAccount_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(contributionRepository.findByUser(user)).thenReturn(contribution);

        ResponseEntity<String> response = contributionService.moneyToTheAccount("testuser");
        assertEquals("Вы положили деньги со вклада на свой счет: 1500", response.getBody());
        verify(userRepository, times(1)).save(any(User.class));
        verify(contributionRepository, times(1)).delete(any(Contribution.class));
    }

    @Test
    void testShowMoneyInContribution_noContribution() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(contributionRepository.findBalanceByUser(user)).thenReturn(null);

        ResponseEntity<?> response = contributionService.showMoneyInContribution("testuser");
        assertEquals("У вас нет вклада.", response.getBody());
    }

    @Test
    void testShowMoneyInContribution_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(contributionRepository.findBalanceByUser(user)).thenReturn(BigDecimal.valueOf(500));

        ResponseEntity<?> response = contributionService.showMoneyInContribution("testuser");
        assertEquals("На вашем вкладе : 500", response.getBody());
    }

    @Test
    void testIncreaseBalance_existingContribution() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(contributionRepository.findByUser(user)).thenReturn(contribution);

        ResponseEntity<String> response = contributionService.increaseBalance("testuser", BigDecimal.valueOf(300));
        assertEquals("Ошибка, возможно вы хотите сделать второй вклад", response.getBody());
    }

    @Test
    void testIncreaseBalance_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(contributionRepository.findByUser(user)).thenReturn(null);

        ResponseEntity<String> response = contributionService.increaseBalance("testuser", BigDecimal.valueOf(300));
        assertEquals("Вы сделали вклад на сумму: 300", response.getBody());
        verify(contributionRepository, times(1)).save(any(Contribution.class));
        verify(userRepository, times(1)).save(any(User.class));
    }
}

