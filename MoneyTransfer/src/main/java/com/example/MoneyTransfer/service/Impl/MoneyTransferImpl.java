package com.example.MoneyTransfer.service.Impl;


import com.example.MoneyTransfer.config.KafkaConsumerConfig;
import com.example.MoneyTransfer.model.MoneyTransfer;
import com.example.MoneyTransfer.repository.MoneyTransferRepository;
import com.example.MoneyTransfer.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
public class MoneyTransferImpl implements MoneyTransferService {
    @Autowired
    private MoneyTransferRepository moneyTransferRepository;
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Override
    public void saveToDb(String clientusername,String clientrecipient, BigDecimal amountofmoney) {
        try {

            MoneyTransfer db = MoneyTransfer.builder()
                    .clientusername(clientusername)
                    .clientrecipient(clientrecipient)
                    .amountofmoney(amountofmoney).build();

            moneyTransferRepository.save(db);



            logger.info("Processing transaction from {} to {} with amount {}");

        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage());
        }


    }



}
