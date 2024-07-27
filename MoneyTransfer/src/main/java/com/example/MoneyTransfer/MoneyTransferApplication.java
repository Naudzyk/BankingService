package com.example.MoneyTransfer;

import com.example.MoneyTransfer.config.SpringConfig;
import com.example.MoneyTransfer.service.MoneyTransferService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class MoneyTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyTransferApplication.class, args);
	}
			ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class); // Загрузка контекста Spring

}
