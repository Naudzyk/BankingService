package com.zhenya.ru.bank.service;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface MoneyTransferService {

    ResponseEntity<?> transfer(String user, String getUser , BigDecimal money);
}
