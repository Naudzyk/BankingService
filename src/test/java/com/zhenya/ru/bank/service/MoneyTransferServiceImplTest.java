package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.dto.MoneyTransferDTO;
import com.zhenya.ru.bank.exception.InsufficientBalanceException;
import com.zhenya.ru.bank.exception.UserNotFoundException;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.impl.MoneyTransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class MoneyTransferServiceImplTest {
    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaTemplate<String, MoneyTransferDTO> kafkaTemplate;

    @InjectMocks
    private MoneyTransferServiceImpl moneyTransferService;

    private User userFrom;
    private User userTo;

    @BeforeEach
    public void setUp() {
        userFrom = User.builder()
                .id(1L)
                .username("fromUser")
                .balance(new BigDecimal("1000"))
                .build();

        userTo = User.builder()
                .id(2L)
                .username("toUser")
                .balance(new BigDecimal("500"))
                .build();
    }

    @Test
    public void testTransferSuccess() {
        when(userService.getUserByUsername("fromUser")).thenReturn(Optional.of(userFrom));
        when(userService.getUserByUsername("toUser")).thenReturn(Optional.of(userTo));
        when(userRepository.findById(userFrom.getId())).thenReturn(Optional.of(userFrom));
        when(userRepository.findById(userTo.getId())).thenReturn(Optional.of(userTo));

        moneyTransferService.transfer("fromUser", "toUser", new BigDecimal("200"));

        assertEquals(new BigDecimal("800"), userFrom.getBalance());
        assertEquals(new BigDecimal("700"), userTo.getBalance());

        verify(userRepository, times(1)).save(userFrom);
        verify(userRepository, times(1)).save(userTo);

        verify(kafkaTemplate, times(1)).send(eq("money-transfer-topic"), any(MoneyTransferDTO.class));
    }

    @Test
    public void testTransferInsufficientBalance() {
        when(userService.getUserByUsername("fromUser")).thenReturn(Optional.of(userFrom));
        when(userService.getUserByUsername("toUser")).thenReturn(Optional.of(userTo));
        when(userRepository.findById(userFrom.getId())).thenReturn(Optional.of(userFrom));
        when(userRepository.findById(userTo.getId())).thenReturn(Optional.of(userTo));

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            moneyTransferService.transfer("fromUser", "toUser", new BigDecimal("2000"));
        });

        assertEquals("У пользователя fromUser недостаточно денег", exception.getMessage());

        verify(userRepository, never()).save(userFrom);
        verify(userRepository, never()).save(userTo);
        verify(kafkaTemplate, never()).send(anyString(), any(MoneyTransferDTO.class));
    }

    @Test
    public void testTransferUserNotFound() {
        when(userService.getUserByUsername("fromUser")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            moneyTransferService.transfer("fromUser", "toUser", new BigDecimal("200"));
        });

        assertEquals("Пользователь не найден", exception.getMessage());

        verify(userRepository, never()).save(userFrom);
        verify(userRepository, never()).save(userTo);
        verify(kafkaTemplate, never()).send(anyString(), any(MoneyTransferDTO.class));
    }

    @Test
    public void testTransferRecipientNotFound() {
        when(userService.getUserByUsername("fromUser")).thenReturn(Optional.of(userFrom));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            moneyTransferService.transfer("fromUser", "toUser", new BigDecimal("200"));
        });

        assertEquals("Пользователь не найден", exception.getMessage());

        verify(userRepository, never()).save(userFrom);
        verify(userRepository, never()).save(userTo);
        verify(kafkaTemplate, never()).send(anyString(), any(MoneyTransferDTO.class));
    }

}
