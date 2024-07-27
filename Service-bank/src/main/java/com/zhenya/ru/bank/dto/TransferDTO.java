package com.zhenya.ru.bank.dto;

import java.math.BigDecimal;

public record TransferDTO(
        String getUsername,

        BigDecimal money
) {
}
