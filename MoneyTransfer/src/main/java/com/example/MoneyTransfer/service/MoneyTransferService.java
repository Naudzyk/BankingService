package com.example.MoneyTransfer.service;

import com.example.MoneyTransfer.model.MoneyTransfer;

import java.math.BigDecimal;

public interface MoneyTransferService {

    void saveToDb (String clientusername,
        String clientrecipient,
        BigDecimal amountofmoney);



}
