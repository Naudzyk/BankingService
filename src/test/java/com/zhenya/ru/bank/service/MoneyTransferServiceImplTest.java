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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
                .balance(new BigDecimal("1000"))
                .build();

    }

    @Test
    public void testTransferInsufficientBalance() {
        when(userService.getUserByUsername("fromUser")).thenReturn(Optional.of(userFrom));
        when(userService.getUserByUsername("toUser")).thenReturn(Optional.of(userTo));
        when(userRepository.findById(userFrom.getId())).thenReturn(Optional.of(userFrom));
        when(userRepository.findById(userTo.getId())).thenReturn(Optional.of(userTo));

        ResponseEntity<?> exception =
            moneyTransferService.transfer("fromUser", "toUser", new BigDecimal("2000"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("У пользователя fromUser недостаточно денег", exception.getBody());

        verify(userRepository, never()).save(userFrom);
        verify(userRepository, never()).save(userTo);
        verify(kafkaTemplate, never()).send(anyString(), any(MoneyTransferDTO.class));
    }

    @Test
    public void testSuccessfulTransfer() {
        when(userService.getUserByUsername("fromUser")).thenReturn(Optional.of(userFrom));
        when(userService.getUserByUsername("toUser")).thenReturn(Optional.of(userTo));
        when(userRepository.findById(userFrom.getId())).thenReturn(Optional.of(userFrom));
        when(userRepository.findById(userTo.getId())).thenReturn(Optional.of(userTo));

        CompletableFuture<SendResult<String, MoneyTransferDTO>> future = CompletableFuture.completedFuture(new SendResult<>(null, null));
        when(kafkaTemplate.send(anyString(), any(MoneyTransferDTO.class))).thenReturn(future);


        ResponseEntity<?> responseEntity = moneyTransferService.transfer("fromUser", "toUser", new BigDecimal("500"));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Перевод выполнен от fromUser к toUser на сумму 500", responseEntity.getBody());

        assertEquals(new BigDecimal("500"), userFrom.getBalance());
        assertEquals(new BigDecimal("1500"), userTo.getBalance());

        verify(userRepository, times(1)).save(userFrom);
        verify(userRepository, times(1)).save(userTo);
        verify(kafkaTemplate, times(1)).send(eq("money-transfer-topic"), any(MoneyTransferDTO.class));
    }
    @Test
    public void testUserNotFound() {
        when(userService.getUserByUsername("nonExistingUser")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            moneyTransferService.transfer("nonExistingUser", "toUser", new BigDecimal("500"));
        });

        assertEquals("Пользователь не найден", exception.getMessage());

        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), any(MoneyTransferDTO.class));
    }

}