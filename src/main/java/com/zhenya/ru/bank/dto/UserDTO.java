package com.zhenya.ru.bank.dto;

import com.zhenya.ru.bank.models.UserEmail;
import com.zhenya.ru.bank.models.UserPhones;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public record UserDTO(
        String username,
        String fullname,
        String email,
        String phone,
        LocalDate dateOfBirth,
        BigDecimal balance,

        BigDecimal initialBalance,
        String password
) {
}
