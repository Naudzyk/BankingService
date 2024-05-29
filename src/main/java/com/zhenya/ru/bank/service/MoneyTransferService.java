package com.zhenya.ru.bank.service;

import java.math.BigDecimal;

public interface MoneyTransferService {

    void transfer(String user, String getUser , BigDecimal money);
}
