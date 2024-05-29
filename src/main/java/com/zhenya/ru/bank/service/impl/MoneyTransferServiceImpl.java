package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.exception.UserNotFoundException;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.MoneyTransferService;
import com.zhenya.ru.bank.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final UserService userService;
    private final UserRepository userRepository;


    @Override
    public void transfer(String user, String getUser, BigDecimal money) {
        User userFrom = userRepository.findById(getIdByUsername(user)).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        User userTo = userRepository.findById(getIdByUsername(getUser)).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (userFrom.getBalance().compareTo(money) < 0) {
            throw new RuntimeException("У пользователя недостаточно денег");
        }

        BigDecimal curBalanceTo = userTo.getBalance();
        userTo.setBalance(curBalanceTo.add(money));
        BigDecimal curBalanceFrom = userFrom.getBalance();
        userFrom.setBalance(curBalanceFrom.subtract(money));

        userRepository.save(userFrom);
        userRepository.save(userTo);
    }


    private Integer getIdByUsername(String username) {
        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get().getId();
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
