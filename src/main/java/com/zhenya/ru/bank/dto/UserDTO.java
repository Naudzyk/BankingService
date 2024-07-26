package com.zhenya.ru.bank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.math.BigDecimal;
import java.time.LocalDate;
;

public record UserDTO(
        String username,
        String fullname,
        String email,
        String phone,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth,
        BigDecimal balance,

        BigDecimal initialBalance,
        String password
) {
}
