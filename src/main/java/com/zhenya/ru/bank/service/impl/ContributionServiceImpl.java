package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.models.Contribution;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.repository.ContributionRepository;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.ContributionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ContributionServiceImpl implements ContributionService {


    private final UserRepository userRepository;
    private final ContributionRepository contributionRepository;



    @Override
    public ResponseEntity<String> moneyToTheAccount(String username) {
        User user = userRepository.findByUsername(username);
        Contribution contribution = contributionRepository.findByUser(user);
        if (contribution == null) {
            return ResponseEntity.ok("У вас нету вклада");
        }
        try {
            BigDecimal currentBalance = user.getBalance();
            user.setBalance(currentBalance.add(contribution.getBalance()));
            userRepository.save(user);
            contributionRepository.delete(contribution);
            return ResponseEntity.ok("Вы положили деньги со вклада на свой счет: " + user.getBalance());
        } catch (Exception e) {

            return ResponseEntity.status(500).body("Произошла ошибка при переносе денег со вклада на счет");
        }


    }

    @Override
    public ResponseEntity<?> showMoneyInContribution(String username) {
        User user = userRepository.findByUsername(username);
        BigDecimal balance = contributionRepository.findBalanceByUser(user);
        if (balance == null) {
            return ResponseEntity.ok("У вас нет вклада.");
        }
        return ResponseEntity.ok("На вашем вкладе : " + balance);
    }

    public ResponseEntity<String> increaseBalance(String username, BigDecimal money) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
    }

    Contribution existingContribution = contributionRepository.findByUser(user);
    if (existingContribution != null) {
        return ResponseEntity.badRequest().body("Ошибка, возможно вы хотите сделать второй вклад");
    }

    BigDecimal currentBalance = user.getBalance();
    if (currentBalance.compareTo(money) < 0) {
        return ResponseEntity.badRequest().body("На вашем счете не достаточно средств: " + currentBalance + ". Вы хотите положить: " + money);
    }

    Contribution contribution = Contribution.builder()
            .balance(money)
            .initialBalance(money)
            .user(user)
            .build();
    contributionRepository.save(contribution);
    user.setBalance(currentBalance.subtract(money));
    userRepository.save(user);

    return ResponseEntity.ok("Вы сделали вклад на сумму: " + money);
}


}


