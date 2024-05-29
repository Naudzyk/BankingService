package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.exception.UserNotFoundException;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.ContributionService;
import com.zhenya.ru.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class СontributionServiceImpl {
    private final UserRepository userRepository;
    private final BigDecimal MAX_LIMIT = BigDecimal.valueOf(2.07);
    private final BigDecimal PERCENT_BONUS = BigDecimal.valueOf(1.1);
    @Scheduled(fixedDelay = 30000)
    public void increaseBalance() {
        List<User> users = userRepository.findAll();
        for(User user : users) {
            BigDecimal maxBalance = user.getInitialBalance().multiply(MAX_LIMIT);
            BigDecimal balance = user.getBalance().multiply(PERCENT_BONUS);
            if (balance.compareTo(maxBalance) <= 0) user.setBalance(balance);
        }
            userRepository.saveAll(users);
       }

}
