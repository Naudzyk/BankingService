package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.dto.MoneyTransferDTO;
import com.zhenya.ru.bank.exception.UserNotFoundException;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.MoneyTransferService;
import com.zhenya.ru.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final KafkaTemplate<String,MoneyTransferDTO> kafkaTemplate;

     private static final Logger logger = LoggerFactory.getLogger(MoneyTransferServiceImpl.class);


    @Override
    public ResponseEntity<?> transfer(String fromUser, String toUser, BigDecimal money) {
    User userFrom = userRepository.findById(getIdByUsername(fromUser))
            .orElseThrow(() -> new UserNotFoundException("Пользователь " + fromUser + " не найден"));
    User userTo = userRepository.findById(getIdByUsername(toUser))
            .orElseThrow(() -> new UserNotFoundException("Пользователь " + toUser + " не найден"));

    if (userFrom.getBalance().compareTo(money) < 0) {
        return ResponseEntity.badRequest().body("У пользователя " + fromUser + " недостаточно денег");
    } else {
        userTo.setBalance(userTo.getBalance().add(money));
        userFrom.setBalance(userFrom.getBalance().subtract(money));

        userRepository.save(userFrom);
        userRepository.save(userTo);

        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO(fromUser, toUser, money);
         try {
                kafkaTemplate.send("money-transfer-topic", moneyTransferDTO).get();
                logger.info("Сообщение отправлено в Kafka: " + moneyTransferDTO);
            } catch (Exception e) {
                logger.error("Ошибка при отправке сообщения в Kafka", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при отправке сообщения в Kafka");
            }

        return ResponseEntity.ok("Перевод выполнен от " + fromUser + " к " + toUser + " на сумму " + money);
    }
}


    private Long getIdByUsername(String username) {
        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get().getId();
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
