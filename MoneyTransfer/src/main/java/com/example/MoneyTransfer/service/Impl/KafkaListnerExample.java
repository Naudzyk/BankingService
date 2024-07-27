package com.example.MoneyTransfer.service.Impl;

import com.example.MoneyTransfer.model.MoneyTransferDTO;
import com.example.MoneyTransfer.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListnerExample {
  private final MoneyTransferService moneyTransferService;

    @KafkaListener(topics = "money-transfer-topic", groupId = "myGroup", containerFactory = "kafkaListenerContainerFactory")
    public void listener(MoneyTransferDTO moneyTransfer) {
        try {

            if (moneyTransferService != null) {
                moneyTransferService.saveToDb(moneyTransfer.getClientusername(), moneyTransfer.getClientrecipient(), moneyTransfer.getAmountofmoney());
            } else {
                log.error("MoneyTransferService is null. Cannot proceed with saving to database.");

            }
        } catch (Exception e) {
            log.error("Exception occurred while processing message: {}", e.getMessage());

        }
    }
}

