package com.zhenya.ru.bank.service;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface ContributionService {
    ResponseEntity<?> increaseBalance(String username, BigDecimal money);

    ResponseEntity<String> moneyToTheAccount(String username);

    ResponseEntity<?> showMoneyInContribution(String username);
}
