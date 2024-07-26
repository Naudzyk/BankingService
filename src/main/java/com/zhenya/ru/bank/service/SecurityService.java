package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.dto.TokenDTO;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SecurityService {

    ResponseEntity<?> register(String username , String fullname, String email, String phone, LocalDate date, BigDecimal balance, String password);

    ResponseEntity<TokenDTO> authorize(String username, String password);
}
