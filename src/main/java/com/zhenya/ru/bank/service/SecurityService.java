package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.dto.TokenDTO;
import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserEmail;
import com.zhenya.ru.bank.models.UserPhones;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface SecurityService {

    User register(String username , String fullname, String email, String phone, LocalDate date, BigDecimal balance, String password);

    TokenDTO authorize(String username, String password);
}
